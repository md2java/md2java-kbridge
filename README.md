#md2java-kbridge[ Use bridge application between KAFKA & IBM MQ]
----------------------------------------------------------------
	
	#How to build KAFKA-IBMMQ Bridge application
	
	1) create spring boot web application
	
	2) add md2java-kbridge dependency.
		<dependency>
			<groupId>io.github.md2java</groupId>
			<artifactId>md2java-kbridge</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	3) add avro schema dependecy[as per your requirement]-OPTIONAL
	<dependency>
			<groupId>io.github.avro</groupId>
			<artifactId>avro-schema-model</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	4) in application.properties provide
	spring.config.import=classpath:kbridge.properties,classpath:application-kbridge.properties
	
	5) add @EnableKbridge on main class.
	
	
    6) write your spring component for payload transformation and mapping [kafka to mq]
       1) for kafka to mq use --KafkaPayloadTransformer 
       2) for mq to kafka use --MqPayloadTransformer
       
     
 
    good to have [for spring boot & jdk version management]
    ----------------------------------------------------
     libs configuration[jdk & boot]
		----------------------
		<properties>
				<java.version>1.8</java.version>
				<spring.boot.version>2.7.17</spring.boot.version>
		</properties>
		