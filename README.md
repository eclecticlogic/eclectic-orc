# eclectic.orc
Java object writer for ORC

Two ways to handle enum - declare a converter or add @Orc annotation to a method or do nothing and have name() be used. Converter takes precedence.
Handles list of enums
Supports conversion of each element in the list to a different list type via converter attribute of @OrcCollection.