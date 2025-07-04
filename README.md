# ObjDiffy [![](https://jitpack.io/v/edgar-zigis/ObjDiffy.svg)](https://jitpack.io/#edgar-zigis/ObjDiffy)
KSP object diff generator which can be used for Redux purposes.

## Credits
The initial author of the KAPT based version is ***Lukas Sivickas*** and the library can be found [here](https://github.com/luksiv/entdiffy).

## Gradle
Make sure you have **Jitpack.IO** included in your gradle repositories.

```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```gradle
implementation 'com.github.edgar-zigis.ObjDiffy:annotations:1.0.1'
kapt 'com.github.edgar-zigis.ObjDiffy:processor:1.0.1'
```

## Usage
To start using the library add the annotation `@DiffEntity` to your entity, for example:
```
@DiffEntity
data class Person(
    val name: String,
    val age: Int,
    val work: Work
)

data class Work(
    val position: String,
    val salary: Double
)
```
## Use the generated DiffUtil
The library generates a `DiffUtil` class (in this example `PersonDiffUtil`) which has 1 method named `calculateDiff`:
```
public object PersonDiffUtil {
    public fun calculateDiff(first: Person, second: Person): PersonDiffResult {
        return PersonDiffResult(
            nameChanged = first.name != second.name,
            ageChanged = first.age != second.age,
            workChanged = first.work != second.work
        )
    }
}
```
Or if you prefer using extensions, there are also those:
```
public object PersonDiffUtilExtensions {
    public fun Person.calculateDiff(other: Person?): PersonDiffResult = PersonDiffUtil.calculateDiff(this, other)
}
```
## Results 
The library also generates the results class, which has boolean variables whether a parameter has changed:
```
public data class PersonDiffResult(
    public val nameChanged: Boolean,
    public val ageChanged: Boolean,
    public val workChanged: Boolean
)
```
## Now joining everything together
Code:
```
fun main() {
    val a = Person("James", 20, Work("Android developer", 1000.0))
    val b = Person("James", 30, Work("Head of Mobile", 3000.00))

    println(a)
    println(b)

    println(PersonDiffUtil.calculateDiff(a, b))
}
```
Output:
```
Person(name=James, age=20, work=Work(position=Android developer, salary=1000.0))
Person(name=James, age=30, work=Work(position=Head of Mobile, salary=3000.0))
PersonDiffResult(nameChanged=false, ageChanged=true, workChanged=true)
```
