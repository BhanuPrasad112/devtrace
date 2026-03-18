<div align="center"> DevTrace
</div>


DevTrace a CLI-based tool to capture, inspect, replay and export Http Requests for spring applications.


Features:

    1.Capture Http Requests inside your application.
    2.Start and Stop capturing dynamically.
    3.list all captured requests.
    4.watch the capturing requests in realtime.
    5.inspect specified request with request and response body.
    6.edit and replay requests in your terminal .
    7.Export the requests in json format.
    8.Delete specific or all requests 


Installation:




```markdown
#### 1. Add the Repository
Add this to your `pom.xml` so Maven knows where to find JitPack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

#### 2. Add the Dependency
Then add the DevTrace library:

```xml
<dependency>
    <groupId>com.github.BhanuPrasad112</groupId>
    <artifactId>DevTraceServer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Commands:

| Command     | Description                                                             |
| :--  | :-- |
|`devtrace start` | **Starts** capturing incoming Http requests.                            |
|`devtrace stop` | **Stops** capturing incoming requests.                                  |
|`devtrace status`| Displays the current status of Capture Whether it is **ENABLED** OR NOT.|
|`devtrace watch` | It Streams current captured requests and logs in the termninal.         |
|`devtrace list`| Shows a table of the most recent captured requests                      |
|`devtrace show <id>` | Displays Request and Response Body of that particular ID.               |
|`devtrace replay <id> ` | Re-runs a specific request --> opens an editor to tweak the data.       |
|`devtrace export` | Exports all the captured Requests to Json file.                         |
|`devtrace delete all`| It deletes all the captured requests.                                   |
|`devtrace delete <id>`| It deletes specific id captured request.                                |







    


