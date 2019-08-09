- 转换器分组 
  
- 转换关系映射

    可用@Convertible指定映射关系

    BeanToBeanConverterHandler#convert() 
    
    - 怎么控制一个Bean转换为你的目标Bean

        - @ConvertBean 可根据bean中某些域的值控制其目标bean

        - 使用tip，tip=group

        - 当某些条件成立时，给转换器多个参数（这样不好，会导致转换器的参数列表失去抽象）

        - 转换器可接收多个参数进行转换（同上）

    - 怎么控制一个Bean转换为你的目标Bean，在不使用tip的情况下

        - 默认值，指定一个Bean默认就是转换为某个Bean

        - 给转换器设置策略，
        
@ConvertibleProcessor，可以为@Convertible注解类指定一个加工器，工具类只需要使用这个加工器就能正确的解析转换器

直接从bean的头上根据group解析出一个转换器（BeanToBeanConverterHandler或者其他）来，然后就可以开始对bean进行转换了。
嵌套处理（也就是对各种容器的支持）做成一个可选的开关

- 完成一个域的转换：

    - 指定转换器，分组对应的情况下会被选择
    
    - 默认转换器
    
    - 直接使用原值