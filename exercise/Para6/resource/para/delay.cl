#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Delay(const int width, const int height,
         __global uchar* latest, __global uchar* previous,
         __global uchar* output, const float p){
  int x = get_global_id(0);
  int y = get_global_id(1);

  // output := p * latest + (1 - p) * previous
  output[(y * width + x) * 4] = (p * (uchar)latest[(y * width + x) * 3] + (1 - p) * (uchar)previous[(y * width + x) * 3]);
  output[(y * width + x) * 4 + 1] = (p * (uchar)latest[(y * width + x) * 3 + 1] + (1 - p) * (uchar)previous[(y * width + x) * 3 + 1]);
  output[(y * width + x) * 4 + 2] = (p * (uchar)latest[(y * width + x) * 3 + 2] + (1 - p) * (uchar)previous[(y * width + x) * 3+ 2]);
  output[(y * width + x) * 4 + 3] = 0xff;

  // previous := latest
  previous[(y * width + x) * 3] = (uchar)latest[(y * width + x) * 3];
  previous[(y * width + x) * 3 + 1] = (uchar)latest[(y * width + x) * 3 + 1];
  previous[(y * width + x) * 3 + 2] = (uchar)latest[(y * width + x) * 3 + 2];
}
