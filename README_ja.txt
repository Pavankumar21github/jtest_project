Ant、Maven、Gradle との統合の説明は次のページにあります。
https://docs.parasoft.com/display/JTESTJP20231/Integrating+with+Build+Systems

このドキュメントは、製品に付属の "demo" プロジェクトを使用して、Jtest で静的解析、テスト実行、カバレッジ情報の収集を行う方法を説明します。

このプロジェクトの解析設定は、[INSTALL]/examples/demo ディレクトリにある demo.properties ファイルで
設定されています。
demo.properties ファイルは Ant、Maven、Gradle ビルド スクリプトによって使用されます。
以下のコマンド ラインのサンプルは、"demo" プロジェクト ディレクトリで実行することを
前提にしています。


前提条件
-------------------------------------------------
1. Jtest インストール ディレクトリにある jtestcli.properties で Jtest のライセンスが設定されていること。


Jtest データ ファイル
-------------------------------------------------
1. "Recommended Rules" テスト コンフィギュレーションを実行して、静的解析を直接 (ビルド システムなしで) 行います。 - 次のコマンドを使用します。
   Windows:
     ..\..\jtestcli.exe -config "builtin://Recommended Rules" -data demo.data.json -report report
   UNIX:
     ../../jtestcli -config "builtin://Recommended Rules" -data demo.data.json -report report


Ant
-------------------------------------------------
1. "ant" がパスに含まれていることを確認します。

2. "Demo Configuration" を実行して静的解析を行い、単体テストのカバレッジを収集します。

     ant -file jtest.xml -Djtest.config="builtin://Demo Configuration"

   "demo" プロジェクトがビルドされ、単体テストが実行されます。The Jtest Plugin for Ant がソース コードのコンパイル データとカバレッジ付きの単体テスト結果を収集して解析を行い、レポートを生成します。
   注意:

   静的解析だけを実行するには、次のコマンドを使用します。

     ant -file jtest.xml jtest-sae

   単体テストだけを実行するには、次のコマンドを使用します。

     ant -file jtest.xml jtest-utc

   コンフィギュレーションは jtest.xml で指定されます。- "jtest"、"jtest-sae"、"jtest-utc" ターゲットを参照してください。


Maven
-------------------------------------------------
1. Jtest ユーザー ガイドに従って Maven を設定します。
   https://docs.parasoft.com/display/JTESTJP20231/Configuring+the+Jtest+Plugin+for+Maven

2. "Demo Configuration" を実行して、静的解析を行い、単体テストのカバレッジを収集します。

     mvnw clean test-compile jtest:agent test jtest:jtest -Djtest.config="builtin://Demo Configuration"

   "demo" プロジェクトがビルドされ、単体テストが実行されます。Jtest Plugin for Maven がソース コードのコンパイル データとカバレッジ付きの単体テスト結果を収集して解析を行い、レポートを生成します。

   注意:

   静的解析だけを実行するには、次のコマンドを使用します。

     mvnw jtest:jtest

   デフォルトで "Recommended Rules" テスト コンフィギュレーションが使用されます。

   単体テストだけを実行するには、次のコマンドを使用します。

     mvnw clean test-compile jtest:agent test jtest:jtest -Djtest.config="builtin://Unit Tests"


Gradle
-------------------------------------------------
注意: "demo" プロジェクトは JUnit 5 を使用します。JUnit 5 は Gradle 4.6 以上を必要とします。

1. インストール済みの Jtest パッケージを設定するか、ビルドスクリプトの "jtest" ブロックに設定を追加します。

2. "Demo Configuration" を実行して、静的解析を行い、単体テストのカバレッジを収集します。

     gradlew clean jtest-agent test jtest -Djtest.config="builtin://Demo Configuration"

   "demo" プロジェクトがビルドされ、単体テストが実行されます。Jtest Plugin for Gradle がソース コードのコンパイル データとカバレッジ付きの単体テスト結果を収集して解析を行い、レポートを生成します。

   注意:

   静的解析だけを実行するには、次のコマンドを使用します。

     gradlew clean assemble jtest

   デフォルトで "Recommended Rules" テスト コンフィギュレーションが使用されます。

   単体テストだけを実行するには、次のコマンドを使用します。

     gradlew clean jtest-agent test jtest -Djtest.config="builtin://Unit Tests"


=================================================

テスト影響分析

テスト影響分析は Maven および Gradle でサポートされています。

Windows:

1. Maven または Gradle でテストを行い、テストおよびカバレッジに関するベースライン データを収集します。
   mvnw clean test-compile jtest:agent test jtest:jtest -Djtest.config="builtin://Unit Tests" -Djtest.report=tia
   または
   gradlew clean jtest-agent test jtest -Djtest.config="builtin://Unit Tests" -Djtest.report=tia

   注意: 結果として、'tia' サブフォルダーに report.xml および coverage.xml ファイルが作成されます。

2. テスト スコープのソース ファイルを変更します。
   src\main\java\examples\mock\FileExample.java

3. 次のコマンドを使用して、変更の影響を受けるテストを実行します。
   mvnw clean tia:affected-tests test -Djtest.referenceCoverageFile=tia/coverage.xml -Djtest.referenceReportFile=tia/report.xml
   または
   gradlew clean affectedTests test -Djtest.referenceCoverageFile=tia/coverage.xml -Djtest.referenceReportFile=tia/report.xml

   コードの変更の影響を受けないテストは実行されません。

UNIX:

1. Maven または Gradle でテストを行い、テストおよびカバレッジに関するベースライン データを収集します。
   ./mvnw clean test-compile jtest:agent test jtest:jtest -Djtest.config="builtin://Unit Tests" -Djtest.report=tia
   または
   ./gradlew clean jtest-agent test jtest -Djtest.config="builtin://Unit Tests" -Djtest.report=tia

   注意: 結果として、'tia' サブフォルダーに report.xml および coverage.xml ファイルが作成されます。

2. テスト スコープのソース ファイルを変更します。
   src/main/java/examples/mock/FileExample.java

3. 次のコマンドを使用して、変更の影響を受けるテストを実行します。
   ./mvnw clean tia:affected-tests test -Djtest.referenceCoverageFile=tia/coverage.xml -Djtest.referenceReportFile=tia/report.xml
   または
   ./gradlew clean affectedTests test -Djtest.referenceCoverageFile=tia/coverage.xml -Djtest.referenceReportFile=tia/report.xml

   コードの変更の影響を受けないテストは実行されません。


詳細は以下を参照してください。
   https://docs.parasoft.com/display/JTESTJP20231/Testing+and+Analysis+with+Maven
   https://docs.parasoft.com/display/JTESTJP20231/Testing+and+Analysis+with+Gradle

=================================================

アプリケーション カバレッジの収集

Windows:

1. アプリケーションをビルドし、モニタリングに必要な情報を収集します。
     ant -file jtest.xml clean jtest-monitor
     または
     mvnw clean package jtest:monitor    
     または
     gradlew clean assemble jtest-monitor
     
   注意: 結果として monitor.zip ファイルが作成されます。

2. アプリケーションを実行してカバレッジ データを収集します。
     
   a) monitor.zip アーカイブを "demo" ディレクトリに展開します ("monitor" サブディレクトリが作成されます)。
      ant:
        Archive path: parasoft\jtest-monitor\monitor.zip
      mvn:
        Archive path: target\jtest\monitor\monitor.zip
      gradle:
        Archive path: build\jtest\monitor.zip
     
   b) agent.bat を実行します。
      cd monitor
      agent.bat
      cd ..
      
   c) b) で生成された Java VM 引数を使用してアプリケーションを実行します。
      ant
        java -cp demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine
      mvn:
        java -cp target\Demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine
      gradle:
        java -cp build\libs\demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine

   d) "Stack Machine Example" アプリケーションを操作します。
      - "Input" フィールドに数字 123 を入力します。
      - "push" ボタンを 5 回押します。
      - "+"、"-"、"x"、"/" ボタンを押します。
      - アプリケーションを終了します。

3. カバレッジ レポートを生成します。

     ..\..\jtestcli.exe -config "builtin://Calculate Application Coverage" -staticcoverage monitor\static_coverage.xml -runtimecoverage monitor\runtime_coverage

     カバレッジの詳細が report.html に出力されます。

UNIX:

1. アプリケーションをビルドし、モニタリングに必要な情報を収集します。
     ant -file jtest.xml clean jtest-monitor
     または
     ./mvnw clean package jtest:monitor    
     または
     ./gradlew clean build jtest-monitor
     
   注意: 結果として monitor.zip ファイルが作成されます。

2. アプリケーションを実行してカバレッジ データを収集します。
   
   a) monitor.zip アーカイブを "demo" ディレクトリに展開します ("monitor" サブディレクトリが作成されます)。
        unzip ./parasoft/jtest-monitor/monitor.zip
      mvn: 
        unzip ./target/jtest/monitor/monitor.zip
      gradle:
        unzip ./build/jtest/monitor.zip
     
   b) agent.sh を実行します。
      ./monitor/agent.sh
      
   c) b) で生成された Java VM 引数を使用してアプリケーションを実行します。
      ant
        java -cp ./demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine
      mvn:
        java -cp ./target/Demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine
      gradle:
        java -cp ./build/libs/demo.jar [b で生成された引数を貼り付ける] examples.stackmachine.RunnableStackMachine

   d) "Stack Machine Example" アプリケーションを操作します。
      - "Input" フィールドに数字 123 を入力します。
      - "push" ボタンを 5 回押します。
      - "+"、"-"、"x"、"/" ボタンを押します。
      - アプリケーションを終了します。

3. カバレッジ レポートを生成します。

     ../../jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage ./monitor/static_coverage.xml -runtimecoverage ./monitor/runtime_coverage

     カバレッジの詳細が report.html に出力されます。


詳細は https://docs.parasoft.com/display/JTESTJP20231/Application+Coverage を参照してください。
