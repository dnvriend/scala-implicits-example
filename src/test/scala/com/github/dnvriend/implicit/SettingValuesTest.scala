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

class SettingValuesTest extends TestSpec {

  it should "be able to call a method explicitly" in {
    def foo(x: String, y: String): String = x + y
    foo("a", "b") shouldBe "ab"
  }

  it should "be possible to curry a method" in {
    def foo(x: String)(y: String): String = x + y
    foo("a")("b") shouldBe "ab"
  }

  it should "be possible to label the last parameter list implicit and still call the method explicit" in {
    def foo(x: String)(implicit y: String): String = x + y
    foo("a")("b") shouldBe "ab"
  }

  it should "be possible to label the last parameter list and call the method with an implicit value" in {
    def foo(x: String)(implicit y: String): String = x + y
    implicit val str = "b"
    foo("a") shouldBe "ab"
  }

  it should "still be possible to explicitly set the implicit parameter" in {
    def foo(x: String)(implicit y: String): String = x + y
    implicit val str = "b"
    foo("a")("c") shouldBe "ac"
  }

  it should "not compile when creating methods with ambiguous implicit parameter lists" in {
    """
      def foo(implicit x: String, y: String): String = x + y
      implicit val a = "a"
      implicit val b = "b"
      foo
    """ shouldNot compile
  }

  it should "compile when having non-ambiguous parameter list" in {
    def foo(implicit x: String, y: Int): String = x + y
    implicit val a = "a"
    implicit val b = 1
    foo shouldBe "a1"
  }
}
