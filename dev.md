- 添加对枚举的支持（当容器中存放的是枚举，应该怎么处理？）

- 能否统一BeanConverter 和 Converter

- tip可能需要一个tipClass

- ConvertibleBean这个注解，好像也可以指定转换器

- 提供一系列语法糖注解

- 自动根据目标类型选择group进行转换(有待深思)

- 能否通过“目标Bean”来指导转换过程

- ConvertibleBeanConverterHandler 提供对特殊转换目标的支持，如Map

- 考虑，如果有部分代码需要定制默认转换，而又不想影响全局，怎么办？