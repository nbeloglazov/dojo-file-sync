# file-sync

Simple file sync tool hacked during London Clojure Dojo at uSwitch. It consists of server and client: 

* server sends changed files to all connected clients;
* clients listen to file updates from server and update their local folder accordingly;

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
