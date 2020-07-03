Cómo afectan los "priorities" al algoritmo actual?

Al tener una lista de prioridades cuya propiedad principal del objeto interno es el
 "part number".

 Esto nos dice que esas partes deben ir sí o sí antes que todo.

¿Cómo hacemos que queden al principio? Esto es lo que se me ocurre, quizás no muy elocuente o brillante en un principio
pero ya iremos afinando la idea.

El método para construir los planes recibe un List de priorities:
```java
public static List<WorkOrderInformation> buildPlanForTwoTurns(
    final String workCenter
    , final List<WorkOrderInformation> workOrderItems
    , final List<Priority> priorities
) {
```

Un objeto de tipo **Priority** consta de dos prioridades:

private String partNumber;
private int order;

Esencialmente la propiedad "partNumber" nos servirá para obtener los items correspondientes de otro parámetro del método:
```java
, final List<WorkOrderInformation> workOrderItems
```

## Pensando en alternativas.

La lista de prioridades debe de:
1) Identificar los items de **workOrderItems**, ponerlos en una lista temporal
1.1) Con identificar me refiero a obtener los items que tengan en el mismo part number.
2) Ordenar estos items por age.
2.1) Agruparlos
```java
final List<WorkOrderInformation> workOrderItems = Utils.sortAndGroup(items, new AgeComparator());
```
3) Remover los items identificados de la lista original (**workOrderItems**) porque si no quedarían duplicados ...
4) Unir las listas ya agrupadas individualmente y dejar el algoritmo correr.

**Estos son los items:**

### Prioridades
```java
final List<Priority> priorities = List.of(new Priority("PT_2", -1), new Priority("PT_5", -1));
```

Estos items van primero.

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

```
## Primer turno -> 8.5 hrs
## Segundo turno -> 8.1 hrs
## Tercer turno -> 5.5 hrs
```

WOInfo{wcDescription=null, partNumber=PT_5, workOrder=WO_5, runHours=0.7, setupHours=0.5, qty=240, age=6, salesPrice=0.0, turn=FIRST, day=MONDAY, machine=} // 0
// 0.5 + 0.7 = 1.2,             sum = 1.2 < FT, Turn.FIRST, Day.MONDAY
WOInfo{wcDescription=null, partNumber=PT_2, workOrder=WO_2, runHours=1.1, setupHours=2.3, qty=20, age=2, salesPrice=0.0, turn=FIRST, day=MONDAY, machine=} // 1
// 2.3 + 1.1 = 3.4, sum = 1.2 + 3.4 = 4.6 < FT, Turn.FIRST, Day.MONDAY
WOInfo{wcDescription=null, partNumber=PT_9, workOrder=WO_9, runHours=1.0, setupHours=1.5, qty=45, age=12, salesPrice=0.0, turn=FIRST, day=MONDAY, machine=} // 2
// 1.5 + 1.0 = 2.5, sum = 1.2 + 3.4 + 2.5 = 7.1 < Turn.FIRST, Day.MONDAY
WOInfo{wcDescription=null, partNumber=PT_6, workOrder=WO_6, runHours=2.5, setupHours=2.0, qty=55, age=8, salesPrice=0.0, turn=SECOND, day=MONDAY, machine=} // 3
// 2.0 + 2.5 = 4.5, sum = 1.2 + 3.4 + 2.5 + 4.5 = 11.6 > FT, Turn.SECOND, Day.MONDAY
WOInfo{wcDescription=null, partNumber=PT_3, workOrder=WO_3, runHours=0.2, setupHours=0.5, qty=14, age=6, salesPrice=0.0, turn=SECOND, day=MONDAY, machine=} // 4
// 0.5 + 0.2 = 0.7, sum = 1.2 + 3.4 + 2.5 + 4.5 + 0.7 = 12.3 > FT, Turn.SECOND, Day.MONDAY
WOInfo{wcDescription=null, partNumber=PT_3, workOrder=WO_3, runHours=1.4, setupHours=0.0, qty=94, age=4, salesPrice=0.0, turn=SECOND, day=MONDAY, machine=} // 5
// 0.0 + 1.4 = 1.4, sum = 1.2 + 3.4 + 2.5 + 4.5 + 0.7 + 1.4 = 13.7 > FT, Turn.SECOND, Day.MONDAY

WOInfo{wcDescription=null, partNumber=PT_10, workOrder=WO_11, runHours=0.5, setupHours=2.5, qty=96, age=6, salesPrice=0.0, turn=FIRST, day=TUESDAY, machine=} // 6
// 2.5 + 0.5 = 3.0, sum = 1.2 + 3.4 + 2.5 + 4.5 + 0.7 + 1.4 + 3.0 = 16.7 > (FT + ST), day change, Turn.FIRST, Day.TUESDAY

WOInfo{wcDescription=null, partNumber=PT_10, workOrder=WO_10, runHours=0.5, setupHours=0.0, qty=12, age=5, salesPrice=0.0, turn=FIRST, day=TUESDAY, machine=} // 7
// 0.0 + 0.5 = 0.5, sum = 0.5 < FT, Turn.FIRST, Day.TUESDAY

WOInfo{wcDescription=null, partNumber=PT_4, workOrder=WO_4, runHours=3.4, setupHours=3.3, qty=14, age=5, salesPrice=0.0, turn=FIRST, day=TUESDAY, machine=}  // 8
// 3.3 + 3.4 = 6.7, sum = 0.5 + 6.7 = 7.2 < FT, Turn.FIRST, Day.TUESDAY

WOInfo{wcDescription=null, partNumber=PT_4, workOrder=WO_4, runHours=5.7, setupHours=0.0, qty=24, age=1, salesPrice=0.0, turn=SECOND, day=TUESDAY, machine=}   // 9
// 0.0 + 5.7 = 5.7, sum = 0.5 + 6.7 + 5.7 = 12.9 > FT, Turn.SECOND, Day.TUESDAY

WOInfo{wcDescription=null, partNumber=PT_4, workOrder=WO_4, runHours=5.7, setupHours=0.0, qty=24, age=1, salesPrice=0.0, turn=FIRST, day=WEDNESDAY, machine=}   // 10
// 0.0 + 5.7 = 5.7, sum = 0.5 + 6.7 + 5.7 + 5.7 = 18.6 > (FT + ST), day change, Turn.FIRST, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_7, workOrder=WO_7, runHours=0.8, setupHours=0.5, qty=40, age=5, salesPrice=0.0, turn=FIRST, day=WEDNESDAY, machine=}   // 11
// 0.5 + 0.8 = 1.3, sum = 1.3, Turn.FIRST, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_12, workOrder=WO_13, runHours=1.5, setupHours=0.8, qty=47, age=5, salesPrice=0.0, turn=FIRST, day=WEDNESDAY, machine=}     // 12
// 0.8 + 1.5 = 2.3, sum = 1.3 + 2.3 = 3.6 < FT, Turn.FIRST, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_8, workOrder=WO_8, runHours=1.0, setupHours=1.0, qty=30, age=4, salesPrice=0.0, turn=FIRST, day=WEDNESDAY, machine=}       // 13
// 1.0 + 1.0 = 2.0, sum = 1.3 + 2.3 + 2.0 = 5.6 < FT, Turn.FIRST, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_8, workOrder=WO_8, runHours=1.3, setupHours=0.0, qty=39, age=3, salesPrice=0.0, turn=FIRST, day=WEDNESDAY, machine=}           // 14
// 0.0 + 1.3 = 1.3, sum = 1.3 + 2.3 + 2.0 + 1.3 = 6.9, Turn.FIRST, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_1, workOrder=WO_1, runHours=4.2, setupHours=0.3, qty=7, age=3, salesPrice=0.0, turn=SECOND, day=WEDNESDAY, machine=}       // 15
// 0.3 + 4.2 = 4.5, sum = 1.3 + 2.3 + 2.0 + 1.3 + 4.5 = 11.4 > FT, Turn.SECOND, Day.WEDNESDAY

WOInfo{wcDescription=null, partNumber=PT_11, workOrder=WO_12, runHours=1.1, setupHours=0.8, qty=22, age=3, salesPrice=0.0, turn=SECOND, day=WEDNESDAY, machine=}        // 16
// 0.8 + 1.1 = 1.9, sum = 1.3 + 2.3 + 2.0 + 1.3 + 4.5 + 1.9 = 13.3 > FT, Turn.FIRST, Day._WEDNESDAY
