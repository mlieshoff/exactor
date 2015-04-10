# Random values in parameters #

You can use a couple of random values for parameter values. As example you have an ACT file like this:

```
MyCoolCommand         name=#a_word# age=#an_int#
MyRollingDiceCommand  #a_d20#   
```

In **MyCoolCommand** the field _name_ will be filled with a random alphabetic string of random length **50** and the field _age_ will be filled with a random integer value.

In **MyRollingDiceCommand** there will be a parameter of a random value by a 20 sides dice.

With this feature test fixture will be undependent from stuff like **name=abbas** and so on, you can easy say, a thing is like a word.

## All supported shortcuts ##

| **Shortcut**    | **Values** |
|:----------------|:-----------|
| a\_boolean   | true, false |
| a\_byte      | Byte.MIN\_VALUE..Byte.MAX\_VALUE |
| a\_d4        | 1..4 |
| a\_d6        | 1..6 |
| a\_d8        | 1..8 |
| a\_d10       | 1..10 |
| a\_d12       | 1..12 |
| a\_d20       | 1..20 |
| a\_d100      | 1..100 |
| a\_double    | Double.MIN\_VALUE..Double.MAX\_VALUE |
| a\_float     | Float.MIN\_VALUE..Float.MAX\_VALUE |
| a\_long      | Long.MIN\_VALUE..Long.MAX\_VALUE |
| a\_short     | Short.MIN\_VALUE..Short.MAX\_VALUE |
| a\_string    | ASCII |
| a\_word      | [a..z,, A..Z] |
| an\_alphaword| [0..9, a-z, A-Z] |
| an\_int      | Integer.MIN\_VALUE..Integer.MAX\_VALUE |