#spring bean 问题

1.  @Autowired 就不用配置 property ref 了
2.   @Autowired  要配置用那个包扫描 <context:component-scan base-package="com.alibaba.migrate"></context:component-scan>
3.  注册不了map的key
	
		<util:map id="dataSaveServiceProvider">
			<entry key="DST_DB" value-ref="dbDataSaveService" />
			<entry key="DST_HSF" value-ref="hsfDataSaveService" />
		</util:map> 