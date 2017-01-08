/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend.`implicit`

import com.github.dnvriend.TestSpec

import scala.language.implicitConversions

object Foo {
  implicit def fromInt(x: Int): Foo =
    Foo(x)
  implicit def fromString(str: String): Foo =
    Foo(Integer.parseInt(str))
  implicit def fromBar(bar: Bar): Foo =
    Foo(bar.x)
}
case class Foo(x: Int)
object Bar {
  implicit def fromFoo(foo: Foo): Bar =
    Bar(foo.x)
}
case class Bar(x: Int)

object Baz {
  implicit def toInt(baz: Baz): Int = baz.x
  implicit def toString(baz: Baz): String = baz.x.toString
  implicit def toFoo(baz: Baz): Foo = Foo(baz.x)
  implicit def toBar(baz: Baz): Bar = Bar(baz.x)
}
case class Baz(x: Int)

class ConvertingTypesTest extends TestSpec {
  it should "be possible to implicit convert a type using Predef" in {
    1 shouldBe a[Integer]
    1L should not be a[Integer]
    1 should not be a[java.lang.Long]
    val x: Long = 1
    x shouldBe a[java.lang.Long]
  }

  // so it is possible to 'direct' the conversion by explicitly telling the compiler which types we want to have.
  // Does this also work on methods?

  it should "be possible to use the return type on a method to 'direct' the conversion" in {
    def foo(x: Int): Long = x
    val x: Int = 1
    val y: Long = foo(x)
    y shouldBe a[java.lang.Long]
  }

  // the compiler can help us convert from other types

  it should "be possible to convert a String to an Int" in {
    implicit def fromStringToInt(str: String): Int = Integer.parseInt(str)
    val x: Int = "1"
    x shouldBe a[java.lang.Integer]

    // of course we can also convert using an implicit notation
    fromStringToInt("1") shouldBe 1
  }

  // can we convert to custom types?

  it should "be possible to convert to another type by putting the conversion methods in the destination types" in {
    // no conversion method is defined in this test, so the compiler will still try to be helpful
    // and look for conversion methods in the type we go to, which is Foo, there all implicit conversions
    // are registered that go from a String, Int or a Bar to a Foo.
    // conversion is to put all conversion from an other type to a 'Foo' in the Foo companion object.
    val a: Foo = 1
    val b: Foo = "1"
    val c: Foo = Bar(1)
    val d: Bar = a
    val e: Bar = b
    val f: Bar = c

    // we can also go the other way around, the compiler will look for a conversion method in Bar
    // and it does.
    val g: Bar = Foo(1)
  }

  it should "be possible to convert to another type by putting the conversion methods in the source types" in {
    val a: Int = Baz(1)
    val b: String = Baz(1)
    val c: Foo = Baz(1)
    val d: Bar = Baz(1)
  }
}
