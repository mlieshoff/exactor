First add the repository information to your Maven pom.xml

```
<repository>
    <id>org-mili-repo</id>
    <url>http://dl.bintray.com/mlieshoff/maven</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>
```

Then add following dependency (depends on what you want, as example exactor-core) to your Maven pom.xml

```
<dependency>
    <groupId>exactor.core</groupId>
    <artifactId>exactor-core</artifactId>
    <version>1.1.29</version>
    <type>jar</type>
    <scope>test</scope>
</dependency>
```

Please check out our new versions here:

```
https://bintray.com/mlieshoff/maven/exactor/view
```
