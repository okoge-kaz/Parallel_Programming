# Parallel Programming

2022-2Q Tokyo Institute of Technology

## Java 環境構築

macOS 12.4 aarch64

1. `brew install openjdk`
2. `sudo ln -sfn $(brew --prefix)/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk`

これで環境構築終了、あとはjavafx sdkを指定の方法でダウンロードして指定のディレクトリに配置するだけ


## javac compile option

[oracle reference](https://docs.oracle.com/javase/jp/7/technotes/tools/solaris/javac.html)

- `-Xlint:<name>`: < `name` >の警告を出す。   
課題のMakefileにある `-Xlint:deprecation -Xdiags:verbose -Xlint:unchecked`もこの一種



## java run option


## 