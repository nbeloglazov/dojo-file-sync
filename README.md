# file-sync

A Clojure library designed to ... well, that part is up to you.

## Usage

Start server on port 10000. Server will watch for changes of 'src' folder.
```shell
lein run -m file-sync.server/start-server src 10000
```

Start client which connects to localhost:10000 and saves all files received from server to 'backup' folder.
```shell
lein run -m file-sync.client/start-client backup localhost 10000
```

You should see 'backup' folder created by client. If you change anything in 'src' folder you'll observe same changes made in 'backup'.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
