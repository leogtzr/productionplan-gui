    Lo que recuerdo de los criterios de ordenamiento en la tabla son:
    1) Prioridades              <----- Esto lo cubriremos pronto, este análisis se enfocará principalmente sin prioridades.
    2) Age
    3) Part Numbers iguales deberían de aparecer juntas. 

    No sé porqué mi mente se inclina a pensar que 

|Work Order|Part Number|Run|Setup|Age|
|:----------:|:-----:|:---:|:-----:|:---:|
|wo1       | pt1       |0.5|0.3|6|

```java
final WorkOrderInformation wo1 = new WorkOrderInformation("PT_1", "WO_1");
wo1.setRunHours(4.2D);
wo1.setSetupHours(0.3D);
wo1.setQty(7);
wo1.setTurn(Turn.NA);
wo1.setAge(3);
```

```java
final WorkOrderInformation wo2 = new WorkOrderInformation("PT_2", "WO_2");
wo2.setRunHours(1.1D);
wo2.setSetupHours(2.3D);
wo2.setQty(20);
wo2.setTurn(Turn.NA);
wo2.setAge(2);
```

final WorkOrderInformation wo3 = new WorkOrderInformation("PT_3", "WO_3");
wo3.setRunHours(0.2D);
wo3.setSetupHours(0.5D);
wo3.setQty(14);
wo3.setTurn(Turn.NA);
wo3.setAge(6);

final WorkOrderInformation wo4 = new WorkOrderInformation("PT_3", "WO_3");
wo4.setRunHours(1.4D);
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
wo4.setSetupHours(0.5D);
wo4.setQty(94);
wo4.setTurn(Turn.NA);
wo4.setAge(4);

final WorkOrderInformation wo5 = new WorkOrderInformation("PT_4", "WO_4");
wo5.setRunHours(3.4D);
wo5.setSetupHours(3.3D);
wo5.setQty(14);
wo5.setTurn(Turn.NA);
wo5.setAge(5);

final WorkOrderInformation wo6 = new WorkOrderInformation("PT_4", "WO_4");
wo6.setRunHours(5.7D);
// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
wo6.setSetupHours(3.3D);
wo6.setQty(24);
wo6.setTurn(Turn.NA);
wo6.setAge(1);

// Some of the following items should be set to Day.TUESDAY:
final WorkOrderInformation wo7 = new WorkOrderInformation("PT_5", "WO_5");
wo7.setRunHours(0.7D);
wo7.setSetupHours(0.5D);
wo7.setQty(240);
wo7.setTurn(Turn.NA);
wo7.setAge(6);

final WorkOrderInformation wo8 = new WorkOrderInformation("PT_6", "WO_6");
wo8.setRunHours(2.5D);
wo8.setSetupHours(2.0D);
wo8.setQty(55);
wo8.setTurn(Turn.NA);
wo8.setAge(8);

final WorkOrderInformation wo9 = new WorkOrderInformation("PT_7", "WO_7");
wo9.setRunHours(0.8D);
wo9.setSetupHours(0.5D);
wo9.setQty(40);
wo9.setTurn(Turn.NA);
wo9.setAge(5);

final WorkOrderInformation wo10 = new WorkOrderInformation("PT_8", "WO_8");
wo10.setRunHours(1.3D);
wo10.setSetupHours(1.0D);
wo10.setQty(39);
wo10.setAge(3);

// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
final WorkOrderInformation wo11 = new WorkOrderInformation("PT_8", "WO_8");
wo11.setRunHours(1.0D);
wo11.setSetupHours(1.0D);
wo11.setQty(30);
wo11.setTurn(Turn.NA);
wo11.setAge(4);

final WorkOrderInformation wo12 = new WorkOrderInformation("PT_9", "WO_9");
wo12.setRunHours(1.0D);
wo12.setSetupHours(1.5D);
wo12.setQty(45);
wo12.setTurn(Turn.NA);
wo12.setAge(8);

final WorkOrderInformation wo13 = new WorkOrderInformation("PT_10", "WO_10");
wo13.setRunHours(0.5D);
wo13.setSetupHours(2.0D);
wo13.setQty(12);
wo13.setTurn(Turn.NA);
wo13.setAge(5);

// The following property should be modified and adjusted to 0.0 since the previous item shares the
// same part number
final WorkOrderInformation wo14 = new WorkOrderInformation("PT_10", "WO_11");
wo14.setRunHours(0.5D);
wo14.setSetupHours(2.5D);
wo14.setQty(96);
wo14.setTurn(Turn.NA);
wo14.setAge(6);

// Some of the following items may appear on Wednesday:
final WorkOrderInformation wo15 = new WorkOrderInformation("PT_11", "WO_12");
wo15.setRunHours(1.1D);
wo15.setSetupHours(0.8D);
wo15.setQty(22);
wo15.setTurn(Turn.NA);
wo15.setAge(3);

final WorkOrderInformation wo16 = new WorkOrderInformation("PT_12", "WO_13");
wo16.setRunHours(1.5D);
wo16.setSetupHours(0.8D);
wo16.setQty(47);
wo16.setTurn(Turn.NA);
wo16.setAge(5);