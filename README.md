# bbs

Spring Bootを使用したシンプルな掲示板アプリケーションです。

投稿フォームから名前と本文を入力し、投稿内容を一覧表示するWebアプリケーションとして開発しています。

画面構成をシンプルにし、投稿内容を1画面上で縦方向に閲覧できる構成を予定しています。

## 使用技術

* Java 25
* Spring Boot 4.1.0
* Maven
* Thymeleaf
* Spring Data JPA
* PostgreSQL 18
* JavaScript

## 開発予定

* 投稿登録
* 投稿一覧表示
* 入力バリデーション
* CSRF対策
* Service層を利用した処理構成
* 画面レイアウトの調整
* JUnitによる単体テスト
* PlaywrightによるE2Eテスト

## ブランチ運用

開発では以下のブランチ構成を使用します。

```text
main
└─ develop
   ├─ feature/db-connection
   ├─ feature/post
   ├─ feature/validation
   ├─ feature/ui
   ├─ feature/unit-test
   └─ feature/e2e-test
```

各機能は `feature/*` ブランチで実装し、動作確認後に `develop` ブランチへマージします。

```text
feature/* で実装
↓
develop へマージ
↓
機能実装・テスト完了
↓
develop を main へマージ
```

`main` は安定版、`develop` は開発統合用、`feature/*` は機能単位の作業用ブランチとして運用します。

## Documents

開発時の設定や作業内容は以下に記録しています。

* [開発備忘録](docs/dev-notes.md)
