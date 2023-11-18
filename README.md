#md2java-kbridge[ Use bridge application between KAFKA & IBM MQ]
----------------------------------------------------------------
	
	#How to build KAFKA-IBMMQ Bridge application
	
	1) create spring boot web application
	
	2) add md2java-kbridge dependency.
		<dependency>
			<groupId>io.github.md2java</groupId>
			<artifactId>md2java-kbridge</artifactId>
			<version>1.1.0</version>
		</dependency>
	3) add avro schema dependecy[as per your requirement]-OPTIONAL
	<dependency>
			<groupId>io.github.avro</groupId>
			<artifactId>avro-schema-model</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
	
	4) add @EnableKbridge on main class.
	
	
    5) write your spring component for payload transformation and mapping [kafka to mq]
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
		
		
##version history

	1.0.0:  initial version[support only for IBM mq and kafka with avro schema registry]
	-----------------------
	1) kafka-with-avro-registry to ibm mq bridge 
	2) [ONE to MANY][mq to kafka & kafka to mq] support
	3) retry upto 3 times[interval of 1 sec] -if not success call callback method[handleOnFail] to consumer.
	
	
	1.0.2:[ support string serialization with avro]
	
	1.1.0:[ added active support with ibmmq]
	#app.mq.provider.name=ibmmq/activemq here default is ibmmq

  