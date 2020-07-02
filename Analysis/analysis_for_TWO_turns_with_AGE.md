    Lo que recuerdo de los criterios de ordenamiento en la tabla son:
    1) Prioridades              <----- Esto lo cubriremos pronto, este análisis se enfocará principalmente sin prioridades.
    2) Age
    3) Part Numbers iguales deberían de aparecer juntas. 

    Podemos ordenar por Age primero y luego juntar basado en 

|Part Number|Work Order|Age|Work Center|...|
|:----------:|:-----:|:---:|:-----:|:---:|
|pt1       | wo1       |9|DOBLADO|,,,|
|pt2       | wo2       |3|DOBLADO|,,,|
|pt3       | wo2       |6|DOBLADO|,,,|
|...       | ...       |...|...|...|
|...       | ...       |...|...|...|
|...       | ...       |...|...|...|
|pt1       | wo9       |5|DOBLADO|,,,|

Aquí la confusión era al orden por Age podría poner las partes separadas.
Luego al ordenar por partes es posible que queden desacomodados ...

**Miriam** confirmó que lo siguiente es correcto:

|Part Number|Work Order|Age|Work Center|...|
|:----------:|:-----:|:---:|:-----:|:---:|
|pt1       | wo1       |9|DOBLADO|,,,|
|pt1       | wo9       |5|DOBLADO|,,,|
|pt3       | wo3       |6|DOBLADO|,,,|
|pt2       | wo2       |3|DOBLADO|,,,|
|...       | ...       |...|...|...|
|...       | ...       |...|...|...|
|...       | ...       |...|...|...|

Fíjate cómo ordené "pt1" en los dos primeros rows, pero si te fijas el "Age" ya no coincide en el segundo renglón, es decir, es un 5, el cual es menor al 6 de "pt3". 

```java
final WorkOrderInformation wo1 = new WorkOrderInformation("PT_1", "WO_1");
wo1.setRunHours(4.2D);
wo1.setSetupHours(0.3D);
wo1.setQty(7);
wo1.setAge(3);
```

```java
final WorkOrderInformation wo2 = new WorkOrderInformation("PT_2", "WO_2");
wo2.setRunHours(1.1D);
wo2.setSetupHours(2.3D);
wo2.setQty(20);
wo2.setAge(2);
```

```java
final WorkOrderInformation wo3 = new WorkOrderInformation("PT_3", "WO_3");
wo3.setRunHours(0.2D);
wo3.setSetupHours(0.5D);
wo3.setQty(14);
wo3.setAge(6);
```

```java
final WorkOrderInformation wo4 = new WorkOrderInformation("PT_4", "WO_4");
wo4.setRunHours(3.4D);
wo4.setSetupHours(3.3D);
wo4.setQty(14);
wo4.setAge(5);
```

```java
final WorkOrderInformation wo5 = new WorkOrderInformation("PT_4", "WO_4");
wo5.setRunHours(5.7D);
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
wo5.setSetupHours(3.3D);
wo5.setQty(24);
wo5.setAge(1);
```

```java
// Some of the following items should be set to Day.TUESDAY:
final WorkOrderInformation wo6 = new WorkOrderInformation("PT_5", "WO_5");
wo6.setRunHours(0.7D);
wo6.setSetupHours(0.5D);
wo6.setQty(240);
wo6.setAge(6);
```

```java
final WorkOrderInformation wo7 = new WorkOrderInformation("PT_6", "WO_6");
wo7.setRunHours(2.5D);
wo7.setSetupHours(2.0D);
wo7.setQty(55);
wo7.setAge(8);
```

```java
final WorkOrderInformation wo8 = new WorkOrderInformation("PT_7", "WO_7");
wo8.setRunHours(0.8D);
wo8.setSetupHours(0.5D);
wo8.setQty(40);
wo8.setAge(5);
```

```java
final WorkOrderInformation wo9 = new WorkOrderInformation("PT_8", "WO_8");
wo9.setRunHours(1.3D);
wo9.setSetupHours(1.0D);
wo9.setQty(39);
wo9.setAge(3);
```

```java
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
final WorkOrderInformation wo10 = new WorkOrderInformation("PT_8", "WO_8");
wo10.setRunHours(1.0D);
wo10.setSetupHours(1.0D);
wo10.setQty(30);
wo10.setAge(4);
```

```java
final WorkOrderInformation wo11 = new WorkOrderInformation("PT_9", "WO_9");
wo11.setRunHours(1.0D);
wo11.setSetupHours(1.5D);
wo11.setQty(45);
wo11.setAge(12);
```

```java
final WorkOrderInformation wo12 = new WorkOrderInformation("PT_10", "WO_10");
wo12.setRunHours(0.5D);
wo12.setSetupHours(2.0D);
wo12.setQty(12);
wo12.setAge(5);
```

```java
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
final WorkOrderInformation wo13 = new WorkOrderInformation("PT_10", "WO_11");
wo13.setRunHours(0.5D);
wo13.setSetupHours(2.5D);
wo13.setQty(96);
wo13.setAge(6);
```

```java
// Some of the following items may appear on Wednesday:
final WorkOrderInformation wo14 = new WorkOrderInformation("PT_11", "WO_12");
wo14.setRunHours(1.1D);
wo14.setSetupHours(0.8D);
wo14.setQty(22);
wo14.setAge(3);
```

```java
final WorkOrderInformation wo15 = new WorkOrderInformation("PT_12", "WO_13");
wo15.setRunHours(1.5D);
wo15.setSetupHours(0.8D);
wo15.setQty(47);
wo15.setAge(5);
```

```java
final WorkOrderInformation wo16 = new WorkOrderInformation("PT_3", "WO_3");
wo16.setRunHours(1.4D);
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
wo16.setSetupHours(0.5D);
wo16.setQty(94);
wo16.setAge(4);
```

```java
final WorkOrderInformation wo17 = new WorkOrderInformation("PT_4", "WO_4");
wo17.setRunHours(5.7D);
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
wo17.setSetupHours(3.3D);
wo17.setQty(24);
wo17.setAge(1);
```

## How should this work

### Results ....

Haciendo un occurrence count análisis nos dá lo siguiente:
```bash
3 PT_4
2 PT_8
2 PT_3
2 PT_10
1 PT_9
1 PT_7
1 PT_6
1 PT_5
1 PT_2
1 PT_12
1 PT_11
1 PT_1
```

Los ages

```bash
wo1.setAge(3);
wo2.setAge(2);
wo3.setAge(6);
wo4.setAge(5);
wo5.setAge(1);
wo6.setAge(6);
wo7.setAge(8);
wo8.setAge(5);
wo9.setAge(3);
wo10.setAge(4);
wo11.setAge(12);
wo12.setAge(5);
wo13.setAge(6);
wo14.setAge(3);
wo15.setAge(5);
wo16.setAge(4);
wo17.setAge(1);
```


