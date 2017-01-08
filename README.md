# scala-implicits-example
A small study project on Scala implicits

## Introduction
[Scala implicits](https://www.scala-lang.org/files/archive/spec/2.12/07-implicits.html) is a language feature of the
Scala programming language. The `implicits` feature is always active and can be used in any program you can think of
so from a simple 'HelloWorld' to a web application.

## What is it all about
Scala implicits is about the behavior of the scala compiler when, during the compilation phase of your Scala source code
the compiler detects that the compiler must do some extra work for us to make the source code actually compile.

The process 'the extra work that the compiler must do for us to make the source code compile' can be summarized in a
single word 'automatic' so something 'automatic' will happen.

In general, the following two 'automatic' things can happen:

1. __Setting of Values:__ Automatic setting parameters on a method with values that we don't have to set ourselves
2. __Conversion of types:__ Automatic conversion of a types so when it is needed for the source code to compile, the compiler can convert
   a type A into a type B for example an Int type into a Long type.

So the basic idea is to make the compiler _automatically_ do something for us, which can be __set-a-value__ or __convert-a-type__.

## Lending a helping hand
The compiler can help by doing something for us automatically like setting a value or converting a type, but of course,
it cannot 'just-do-something' so if it wants to help us, it must be told exactly what do do.

## Telling the compiler that we want it do do things
There is a modifier named `implicit` that we use mark values, methods and classes to be eligable for automatic use by the compiler.
Only when the modifier is used on a value, method or class and the value, method or class can be 'looked-up / found' by the compiler,
then the compiler can do something automatically for us, like setting that found value on a parameter or converting a type.

## Look up rules
Scala implicits is all about doing something automatically for us, and because the compiler must figure out what to do, it will
search for values, methods and classes that have been modified with the `implicit` modifier.

The compiler uses the following look-up rules:

1. First look in current scope
  - Implicits defined in current scope
  - Explicit imports
  - wildcard imports

2. Now look at associated types in
  - Companion objects of a type
  - Implicit scope of an argument's type (2.9.1)
  - Implicit scope of type arguments (2.8.0)
  - Outer objects for nested types

The implicits available under number 1 below has precedence over the ones under number 2.

## Implicit parameters
When we would like to [let the compiler set parameters for us](https://www.scala-lang.org/files/archive/spec/2.12/07-implicits.html#implicit-parameters)
on a method we must modify the parameter list with the modifier `implicit`. A method or constructor can have only one implicit parameter list, and it
must be the last parameter list given.

A method with implicit parameters can be applied to arguments just like a normal method. In this case the implicit label has no effect.
However, if such a method misses arguments for its implicit parameters, such arguments will be automatically provided by the compiler when found.

```scala
scala> implicit val x: Int = 1
x: Int = 1

scala> def foo(x: String)(implicit y: Int): String = s"$x - $y"
foo: (x: String)(implicit y: Int)String

scala> foo("foo")
res0: String = foo - 1
```

## Implicit conversion
When we would like to let [the compiler convert a type A into a type B for us](https://www.scala-lang.org/files/archive/spec/2.12/07-implicits.html#views)
then we must provide a method or a class that has been modified with the keyword `implicit` and that can be used by the compiler to
convert the type from A to B.

```scala
scala> 1
res2: Int = 1

scala> 1: Long
res3: Long = 1
```

A literal like '1' is of type Int. In the [Scala Predef](http://www.scala-lang.org/api/2.12.x/scala/Predef$.html) provides
a method with the implicit modifier that converts the Int to Long.

When no conversion can be found like in the next example, no conversion can be found an therefor the compiler cannot help us.
The compiler will conclude that we don't need its help, and it can only conclude that the expression is what we want. Because
the expression is not correct, the compiler will notify us about the type mismatch:

```
scala> "1": Int
<console>:13: error: type mismatch;
 found   : String("1")
 required: Int
       "1": Int
```

In this example we want to convert a String into an Int, but still use the expression above. To make the expression above
make sense, we need the help of the compiler. We must give it a way to convert the String into an Int:

```scala
scala> import scala.language.implicitConversions
import scala.language.implicitConversions

scala> implicit def stringToInt(str: String): Int = Integer.parseInt(str)
stringToInt: (str: String)Int

scala> "1": Int
res6: Int = 1
```

## The compiler finds more than one way to help us
The compiler can only help us, for example converting a type or provide a value to a method, when it is clear and __unambiguous__,
what it should do. When the compiler finds out there are multiple ways to help us with a certain thing it will tell us that
it cannot help us with the following error __'implicit conversions are not applicable because they are ambiguous'__ which means
that it just doesn't know what to do. The following example shows such a state:

```scala
scala> import scala.language.implicitConversions
import scala.language.implicitConversions

scala> implicit def stringToInt(str: String): Int = Integer.parseInt(str)
stringToInt: (str: String)Int

scala> implicit def strToInt(str: String): Int = Integer.parseInt(str)
strToInt: (str: String)Int

scala> "1": Int
<console>:20: error: type mismatch;
 found   : String("1")
 required: Int
Note that implicit conversions are not applicable because they are ambiguous:
 both method stringToInt of type (str: String)Int
 and method strToInt of type (str: String)Int
 are possible conversion functions from String("1") to Int
       "1": Int
```

What we must do to remove the ambiguity. How to do that depends on how we have structured the solution making use
of the look up rule precedence. For this example it simply means removing the implicit modifier from one of the two
methods.

```scala
scala> import scala.language.implicitConversions
import scala.language.implicitConversions

scala> implicit def stringToInt(str: String): Int = Integer.parseInt(str)
stringToInt: (str: String)Int

scala> def strToInt(str: String): Int = Integer.parseInt(str)
strToInt: (str: String)Int

scala> "1": Int
res0: Int = 1
```

## Resources
- [The official Scala Documentation](https://www.scala-lang.org/documentation/)
- [The official Scala Documentation - Scala Implicits](https://www.scala-lang.org/files/archive/spec/2.12/07-implicits.html)
- [Twitter - Scala School](https://twitter.github.io/scala_school/index.html)

## Books
- [Scala in Action - Nilanjan Raychaudhuri](https://www.manning.com/books/scala-in-action)
- [Scala in Action - Free Chapter 1 - Why Scala?](https://manning-content.s3.amazonaws.com/download/5/7b52dc3-d6ba-4191-a8c8-afca24c4d79e/SiA_sample1.pdf)
- [Scala in Action - Free Chapter 9 - Concurrency Programming In Scala](https://manning-content.s3.amazonaws.com/download/6/6e69907-9297-42b5-a7c3-dd7a70c222ed/SiA_sample9.pdf)
- [Scala in Depth - Joshua Suereth](https://www.manning.com/books/scala-in-depth)
- [Scala in Depth - Free Chapter 5 - Using Implicits - Joshua Suereth](https://manning-content.s3.amazonaws.com/download/5/55171d7-4347-4eef-8c6c-f999485d290b/SiD-Sample05.pdf)
- [Functional Programming in Scala - Paul Chiusano and Rúnar Bjarnason](https://www.manning.com/books/functional-programming-in-scala)
- [Functional Programming in Scala - Free Chapter 1 - What is Functional Programming](https://manning-content.s3.amazonaws.com/download/9/98d542a-f8f9-4cc0-af7a-9a709ac090a4/FPWS_CH01.pdf)
- [Functional Programming in Scala - Free Chapter 10 - Monoids](https://manning-content.s3.amazonaws.com/download/0/3f2c197-2532-4f86-a593-070d1c3a5c56/FPWS_CH10.pdf)

If possible, buy these books, they are available at [manning.com](https://www.manning.com/)
(I have __NO__ affiliation with them, this info is only for your benefit). I would advice to buy these books
when there is a sale, else they are about $50 dollars each, but IMHO well worth it.

## YouTube
- [(1'37 hr) Why Scala? - Venkat Subramaniam](https://www.youtube.com/watch?v=LH75sJAR0hc)
- [(0'57 hr) Functional Thinking - Neil Ford](https://www.youtube.com/watch?v=JeK979aqqqc)
- [(1'04 hr) Learning Functional Programming without Growing a Neckbeard - Pam Grier](https://www.youtube.com/watch?v=OOvL6QAxRK4)
- [(1'42 hr) Scala for the Intrigued - Venkat Subramaniam](https://www.youtube.com/watch?v=grvvKURwGNg)
- [(1'15 hr) Scala with Style - Martin Odersky](https://www.youtube.com/watch?v=kkTFx3-duc8)
- [(0'31 hr) Scala Monads - Dan Rosen](https://www.youtube.com/watch?v=Mw_Jnn_Y5iA)
- [(0'30 hr) Scala Typeclasses - Dan Rosen](https://www.youtube.com/watch?v=sVMES4RZF-8)
- [(0'30 hr) The Typeclass Pattern - An Alternative to Inheritance - Seth Tisue](https://www.youtube.com/watch?v=yYo0gANYViE)
- [(1'33 hr) Part 1 - Scala Implicits, the Real Story - Joshua Suereth and Dick Wall](https://www.youtube.com/watch?v=SH8aGSWJRHE)
- [(1'19 hr) Part 2 - Scala Implicits, the Real Story - Joshua Suereth and Dick Wall](https://www.youtube.com/watch?v=hBjTHl9cA3c)
- [(0'55 hr) Effective Scala - Joshua Suereth](https://www.youtube.com/watch?v=IobLWVuD-CQ)
- [(1'44 hr) Scala - the simple parts - Martin Odersky](https://www.youtube.com/watch?v=ecekSCX3B4Q)
- [(0'50 hr) Scala Tricks - Venkat Subramaniam](https://www.youtube.com/watch?v=tY11Jtj-SA8)
- [(0'48 hr) Being lazy with Scala - Venkat Subramaniam](https://www.youtube.com/watch?v=sf4wU7M0FKA)
- [(3'03 hr) Using Traits, Mixins and Monads in JVM Languages - Venkat Subramaniam](https://www.youtube.com/watch?v=NsZoFno8Mfk)

## SlideShare
- [Scala Implicits - Not to be feared - Derek Wyatt](http://www.slideshare.net/DerekWyatt1/scala-implicits-not-to-be-feared)