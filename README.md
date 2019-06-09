# TODO API

[![CircleCI](https://circleci.com/gh/deka0106/todo-api.svg?style=svg)](https://circleci.com/gh/deka0106/todo-api)

レポート課題: 簡易なTODO管理サービス用のHTTPサーバを作成する

## 概要

* TODOイベントをPOSTで登録，GETで取得，DELETEで削除できる
* POST，GET，DELETEともに，データはJSONでやりとりする
* TODOイベントはメモリ上(H2 Database)に保持する
* 環境として，Ktor+Exposedを利用した

## 実行

```bash
$ gradle run
```

localhost:8080 で起動する

## API仕様

```bash
# イベント登録 API request
POST /api/v1/event
{"deadline": "2019-06-11T14:00:00+09:00", "title": "レポート提出", "memo": ""}

# イベント登録 API response
200 OK
{"status": "success", "message": "registered", "id": 1}

400 Bad Request
{"status": "failure", "message": "invalid date format"}
```

```bash
# イベント全取得 API request
GET /api/v1/event

# イベント全取得 API response
200 OK
{"events": [
    {"id": 1, "deadline": "2019-06-11T14:00:00+09:00", "title": "レポート提出", "memo": ""},
    ...
]}
```

```bash
# イベント1件取得 API request
GET /api/v1/event/${id}

# イベント1件取得 API response
200 OK
{"id": 1, "deadline": "2019-06-11T14:00:00+09:00", "title": "レポート提出", "memo": ""}

404 Not Found
```

```bash
# イベント1件削除 API request
DELETE /api/v1/event/${id}

# イベント1件削除 API response
204 No Content

404 Not Found
```