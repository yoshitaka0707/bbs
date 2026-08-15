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

各ブランチの役割は以下の通りです。

* `main`：安定版・リリース用ブランチ
* `develop`：開発内容を統合するブランチ
* `feature/*`：機能単位の作業ブランチ

`feature/*` ブランチは `develop` から作成し、実装・動作確認後にPull Requestを作成します。

実務でのレビュー工程を想定し、Pull Request上で変更内容を確認できる状態を維持したうえで `develop` へ反映する運用とします。

```text
develop
↓
feature/* で実装
↓
Pull Request作成
↓
変更内容を確認
↓
developへ反映
↓
機能実装・テスト完了
↓
developをmainへ反映
```

### 履歴管理

Gitの履歴は直線的に保ち、**マージコミットは作成しません**。

Pull Request作成後に `develop` が更新された場合は、作業ブランチへ `develop` をマージするのではなく、最新の `develop` に対してrebaseを行います。

```bash
git fetch origin
git rebase origin/develop
```

rebase前の作業ブランチをすでにリモートへpushしている場合は、rebaseによってコミットIDが変更されるため、以下でリモートブランチを更新します。

```bash
git push --force-with-lease
```

`--force` は使用せず、リモートブランチの意図しない上書きを防ぐため `--force-with-lease` を使用します。

Pull Requestから `develop` へ反映する際もマージコミットを作成せず、直線的な履歴を維持します。

マージ済みの `feature/*` ブランチは削除します。

## Documents

開発時の設定や作業内容は以下に記録しています。

* [開発備忘録](docs/dev-notes.md)
