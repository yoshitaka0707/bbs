## Spring Bootプロジェクト作成

Spring Initializrを使用して、Spring Bootプロジェクトを作成した。

![Spring Initializr設定](images/spring_initializr_設定画面.jpg)

## IntelliJ IDEA設定

Project SDKおよびLanguage LevelにJava 25を設定した。

![IntelliJ IDEAプロジェクト設定](images/プロジェクト構造.jpg)

### Git差分確認

IntelliJ IDEAのGit機能を利用し、変更内容の差分が表示されることを確認した。

![IntelliJ Git差分確認](images/差分確認.jpg)


## PostgreSQL設定

PostgreSQL 18をローカル開発環境に導入し、掲示板アプリケーション用のデータベースを作成した。

- Database: bbs
- Port: 5432
- User: postgres

Spring BootのDB接続パスワードは環境変数 `DB_PASSWORD` から取得する。

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bbs
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
