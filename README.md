# StartPointJavaEE
- Is a preconfigure project to start coding ZK + Spring MVC + Hibernate and Postgress Projects.
- It use Maven as dependecy manager.
- Have Eclipse JavaEE IDE.
- Works Linux / Windows / Should works on a Mac.

## Basic Knowledge Recommended to have.
- HTML / CSS / JavaScript, Bootstrap, LESS CSS Preprocesor.
- Console/terminal commands, Git, Maven, Heroku.
- Heroku account validated to be able to use addons, where we're going to use a PostgreSQL addon and DNS Addon for custom domain.
- Console/Terminal commands to move between folders

## Multilayer Arquitecture Web Applications.
- Presentation layer/Front-end: ZK Frameworks.
- Bussines/Logic layer / Back-end: Spring MVC.
- Data Layer: Hibernate.
- Database: PostgreSQL.

## Installation
- You need open a CMD console or the Bash git console.
- cd workspace
- in workspace folder : git clone https://github.com/corderogerardo/startpointjavaee.git
- cd startpointjavaee
- Open Eclipse JavaEE.
- In the menu click option "File", then click "import...", it will open a window to search the project.
- You need to search for the option "Existing Maven Project", click next, to go to the next window where you will browse this project, once you find the this folder, select it and inmediatly it will show you the POM.xml file, A POM file the file that Maven use for projects configurations, add dependencies, plugins.
    - You can check this file, the part I would like you notice is where is the Heroku Plugin. 


## Heroku Installation

- Go to heroku.com, create an account, login.
- Install Heroku Toolbelt, go to your CMD/BASH console: type the command "heroku login", for login with the account you create.
- Go back to the Heroku website, Create new App.
- In your new app go settings, go to the section "Buildpacks", click the button "add buildpacks", paste the following buildpack, https://codon-buildpacks.s3.amazonaws.com/buildpacks/heroku/jvm-common.tgz.
- You need to add the PostgreSQL Addons, open the Heroku PostgreSQL, and search the host, the database name, the username and password, Open pgAdmin the add the HerokuPostgreSQL server to your pgAdmin, search the database and restore the database, this is the same process you did in your local PostgreSQL in pgAdmin.
- In the same settings page, go to the Info sections and search for "Git URL" option, copy that Git URL of your project.
- Go to your cmd/bash console, as we already are in this folder when we clone this repository en move into the folder. 
- You need now:
	- start a git project with the command: "git init"
	- now you need to add the Heroku Git URL with the following command: "git remote add heroku urlgit"

## Folder Structure.
You need to understand how this project is structured
- cd startpointjavaee
    - src (principal folder)
        - main 
            - assembly
            - java
                - business (DAO and Service for CRUD Operations)
                - controller (I recommend you to follow "SoC Separations of Concerns and The Rule of One")
                    - With this I mean that if you're going to work with an inventaries, users, keep them as modules in separate folders, so you can have an order when you're programming, and be able to search and find fast if you need to change something, remember to implement SOLID Principles, Separate in Inteface Classes that functions that has no relations with the class or controller, or you're going to need it in various controllers.
                - entities
                - viewmodel (You're going to work with ZK Framework, I recommend you this approach, because your views stays very clean as your viewmodels, you are able to use binding beetween your viewmodel and view.
            - resource 
                - This folder is IMPORTANT for Hibernate Configurations with Local PostgreSQL and Heroku PostgreSQL, when you IMPORT this Maven Project, the next step is to go to the "startpointjavaee" Folder in ECLIPSE, make a secundary click or right click on this folder, and then look for the option "BUILD PATH -> Configure Build Path", click it and then you will going to have a window with 4 tabs, you need to go to the opcion Tab "source", once there you need to add the resource folder to the Build Path. 
                - The next step is to go to your local PostgreSQL create the Database db and import the database that is in the folder "BD".
                - Now you need to configure your Hibernate Conection to Local PostgreSQL. This part I would teach you in the Next Title, Development Stage.
            - webapp (All the ZK .zul files, bootstrap, content.)
        - BD
        - test

## Development Stage / Deployment to Heroku.

At this point you already need to have installed and configured:

- Restore the db.backup file to the PostgreSQL server instance.
- Must have configured in the Build Path and added the "resource" folder in the "source" Option Tab.
- Go to the Resource Folder in your EclipseJEE IDE, there are 4 .xml files in the resource folder.
	- applicationContext_hib.xml: here is the sessionFactory bean, where you tell Hibernate what package to scan and look for the entities with the annotations, and some HibernateProperties that are useful.
	- jdbc.properties: Here you have the jdbc configurations to conect to your local PostgreSQL, here you will need to change the database name, the username, the password if you set a password to PostgreSQL.
	- springOrmContext.xml: springORM, Look in the DAO for the @Repository annotation and Service @Transaccional annotation.
	- Here is the important file that divide LOCAL FROM REMOTE, Development / Deployment.
	- ###     1. Local / Development - 2. Remote / Deployment
	- - applicationContext.xml:
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:p="http://www.springframework.org/schema/p" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd
           http://www.springframework.org/schema/tx
                   http://www.springframework.org/schema/tx/spring-tx-3.0.xsd">

	<!-- ====================================================== -->
	<!-- Define the property placeholder configurer -->
	<!-- ====================================================== -->
	<bean id="propertyConfigurer"
		class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="location" value="classpath:jdbc.properties" />
	</bean>
<!-- ====================================================== -->
	
	<!-- ====================================================== -->
	<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName" value="${jdbc.driverClassName}" />
		<!-- LOCAL -   -->
<!-- Comment when you're ready for deploy to Heroku(2), UnComment for local(1) use -->
		<property name="url" value="${jdbc.url}" />
		<property name="username" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
<!-- 	UnComment for local use -->
		
<!-- 	HEROKU -->
<!--It works with the BEAN that is just right down this / Funciona junto con el BEAN que sigue despues de esta seccion. -->

<!-- 	Comment for Local(1) Use, UnComment for REMOTE (2) use -->
<!-- 		<property name="url" value="#{ 'jdbc:postgresql://' + @dbUrl.getHost() + ':' + @dbUrl.getPort() + @dbUrl.getPath() }"/> -->
<!--         <property name="username" value="#{ @dbUrl.getUserInfo().split(':')[0] }"/> -->
<!--         <property name="password" value="#{ @dbUrl.getUserInfo().split(':')[1] }"/> -->
<!-- 	UnComment for REMOTE use -->

	</bean>
<!-- IMPORTANT BEAN FOR HEROKU / BEAN IMPORTANTE PARA HEROKU -->
<!-- 	UnComment for REMOTE use only (2) -->
<!-- 	<bean class="java.net.URI" id="dbUrl"> -->
<!--       <constructor-arg value="${DATABASE_URL}"/> -->
<!--     </bean> -->
<!-- 	UnComment for REMOTE use -->
</beans>
```
#### When you're ready for deployment, comment the parts that are marks as Local, and Uncomment the part marks as Remote.

When you finish to configure all the files, the database.
# Final Step for deploy to Heroku.
Go to your CMD/Bash console, move to the folder project, in this case the command "cd startpointjavaee", then you are going to use Maven and the Heroku plugin of Maven.
For Deployment use the command:"mvn clean heroku:deploy-war", remember add the Heroku PostgreSQL Addon, search for the host,database name, username, password, open pgAdmin and add new server in your pgAdmin with these values, restore the database with you have been working in your local machine.

At the end if you did all the steps well, you won't have any problem, when the deployment finish, you need to go in your browser to your Heroku project domain, by example startpointjavaee.herokuapp.com in your browser, if your web application doesn't start at once, wait for 5 minutes and refresh. I have the same problem, in the Bugs section I give more details.

## Working examples
http://quinielasocial.com.ve .

## Bugs
- When you open the web application it thorws an error because the use of dyno slugs on heroku sleeps. You need to open the Web Application, wait for 5-7 minutes and refresh, I think it's the time takes to heroku to wake up the server slug or something like that. If you do know how to fixit, I'm open to contributions.

## How to contribute.

