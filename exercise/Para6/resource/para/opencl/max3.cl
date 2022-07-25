#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Max3(__global float *a, __global float *b, __global float *c, __global float *d, int numElements)
{
    int i = get_global_id(0);
    if (i < numElements)
    {
        d[i] = fmaxf(fmaxf(a[i], b[i]), c[i]);
    }
}