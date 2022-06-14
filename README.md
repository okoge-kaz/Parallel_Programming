# Parallel Programming

2022-2Q Tokyo Institute of Technology

## Java 環境構築

1. javaのバージョン管理のために jenv をインストールする。   
    ```shell
    > brew install jenv
    ```

2. jenv を使うために .zshrc に追記する。
    ```shell
    > echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
    > echo 'eval "$(jenv init -)"' >> ~/.zshrc
    ```

3. .zhsrc をリフレッシュ   
    ```
    > source .zshrc
    ```

4. jenv が正しくインストールされたか確認する。
    ```shell
    > jenv doctor
    [OK]	No JAVA_HOME set
    [ERROR]	Java binary in path is not in the jenv shims.
    ```
    すでにJavaのバイナリが入っていれば ERRORは出ない

5. java本体のダウンロード(brew でいれましょう)
    ```shell
    > brew install java
    ```

6. .zshrcまわり
    ```shell
    > echo 'export PATH="/usr/local/opt/openjdk/bin:$PATH"' >> ~/.zshrc
    > sudo ln -sfn /usr/local/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk
    > echo 'export CPPFLAGS="-I/usr/local/opt/openjdk/include"' >> ~/.zshrc
    ```

    ```shell
    > source .zshrc
    > which java
    /usr/local/opt/openjdk/bin/java
    > java --version
    openjdk 18.0.1.1 2022-04-22
    OpenJDK Runtime Environment Homebrew (build 18.0.1.1+0)
    OpenJDK 64-Bit Server VM Homebrew (build 18.0.1.1+0, mixed mode, sharing)
    ```

7. jenv でバージョン管理する。

    ```shell
    > jenv add /usr/local/opt/openjdk@18/libexec/openjdk.jdk/Contents/Home
    jenv versions
    * system (set by /Users/fujiikazuki/.jenv/version)
    18.0
    18.0.1.1
    openjdk64-18.0.1.1
    ```
  
8. 好きなバージョンを選択する。

    ```shell
    > jenv global 18.0
    ```

