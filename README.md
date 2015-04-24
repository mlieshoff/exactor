# exactor
Automatically exported from code.google.com/p/exactor

#summary How to use in Maven?

First add the repository information to your Maven pom.xml

{{{
<repository>
    <id>org-mili-repo-releases</id>
    <name>Maven Repo for org.mili (releases)</name>
    <url>svn:https://org-mili-repo.googlecode.com/svn/maven-repo/releases</url>
</repository>
}}}

Then add following dependency (depends on what you want, as example exactor-core) to your Maven pom.xml

{{{
<dependency>
    <groupId>exactor.core</groupId>
    <artifactId>exactor-core</artifactId>
    <version>1.1.29</version>
    <type>jar</type>
    <scope>test</scope>
</dependency>
}}}

You will need the wagon connector to access the google repo

{{{
<build>
    <extensions>
        <extension>
            <groupId>org.jvnet.wagon-svn</groupId>
            <artifactId>wagon-svn</artifactId>
            <version>1.9</version>
        </extension>
    </extensions>
</build>
}}}
