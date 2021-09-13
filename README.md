# Создание моков

| Аннтотация | Что делает |
| ------ | ------ |
| `@MockK` | Cоздает мок объекта |
| `@RelaxedMockK` | Cоздает мок объекта, методы возвращают моки своих значений|
| `@SpykK` | Cоздает мок объекта, копирует настоящего объекта |

Для создания моков отмеченных аннотациями необходимо вызвать `MockKAnnotations.init`

| Функция | Что делает |
| ------ | ------ |
| `mockkObject` | позволяет создать мок Enum или Object |
| `mockkConstructor` | позволяет создать мок конструктора объекта|
| `mockkClass` | позволяет создать мок класса. В отличие от `mockK`, принимает ссылку на класс |
| `mockkStatic` | позволяет создать мок функции расширения определенной на уровне модуля. Принимает ссылку на функцию или название модуля в специальном формате |
| `mockK` | позволяет создать мок класса. В отличие от `mockkClass`, принимает параметр типа |
| `spykK` | позволяет создать мок класса и скопировать поля настоящего объекта. |

`spykK` - создает мок копируя поля переданного объекта, либо создает объект используя вызов констуктора без аргументов.

Объект получаемый из `spykK`, `mockK` или `mockkClass` может так же реализовывать интерфейсы, ссылки на которые были переданы при создании мока.  При использовании `spyK` - моков можно получить доступ к закрытым полям объекта. Для этого используются динамические вызовы.

`constructedWith` - следует использовать для того, чтобы задать поведение мока объекта, созданного через указанный конструктор.

`anyConstructed` - следует использовать для того, чтобы задать поведение мока объекта, созданного через любой конструктор.

<br/>

---

# Настройка поведения моков

| Функция | Что делает |
| ------ | ------ |
| `every/coEvery` | позваоляет назначить желаемое поведение указанному моку  |
| `returns` | указывает возвращаемое значение - можно указать списком значения последовательных возвратов|
| `throws` | указывает возвращаемое исключение |
| `answers/coAnswers` | указывает блок кода для выполнения |

<br/>

---

# Проверка вызовов методов

| Функция | Что делает |
| ------ | ------ |
| `verify/coVerify` | проверка вызова метода |

В качестве параметров можно указать минимальное, максимальное или точное число вызовов, время ожидания выполнения проверки.

Так же можно указать тип проверки.

| Тип проверки | Что делает |
| ------ | ------ |
| `ALL`/ `UNORDERED` | порядок вызова методов не важен, должны быть вызваны все методы из списка |
| `ORDERED` | порядок вызова методов важен, должны быть вызваны все методы из списка |
| `SEQUENCE` | порядок вызова методов важен, должны быть вызваны только методы из списка |

Так же существуют `verifyAll/coVerifyAll`, `verifyOrder/coverifyOrder`, `verifySequence/coverifySequence` осуществляющие аналогичные проверки.

Используя вызов `wasNot Called` можно проверить, что мок ни разу не вызывался.

Используя `excludeRecords` можно исключить вызовы из проверки.

`confirmVerified` - вызывает исключение если не все вызовы объекта были обработаны.

<br/>

---

# Работа с varargs

Для описания последовательностей значений в varargs используются:

| Функция | Что делает |
| ------ | ------ |
| `varargAll` | все значения в последовательности удовлетворяют условию |
| `varargAny` | некоторые значения в последовательности удовлетворяют условию |
| `anyVararg` | последовательность любых значений |

Существуют схожие функции для примитивов, например `anyBooleanVararg` или `varargAnyInt`.

<br/>

---

# Валидация аргументов

Для валидации различных аргументов используются матчеры.
Несколько втроенных матчеров:

| Функция | Что делает |
| ------ | ------ |
| `any` | любое значение аргумента |
| `capture` | записывает переданные значения в slot |
| `eq` | точное совпадение аргумента |
| `match/coMatch` | проверка блоком кода |

Свой матчер можно создать реализовав интерфейс `Matcher` либо написав функцию расширения для `MockKVerificationScope` или `MockKMatcherScope`.

<br/>

---

*`co`-префиксы используются для работы с корутинами*
