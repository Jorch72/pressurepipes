/*
 * Copyright (c) bdew, 2013 - 2016
 * https://github.com/bdew/pressure
 *
 * This mod is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://bdew.net/minecraft-mod-public-license/
 */

package net.bdew.pressure.compat.computers

import scala.concurrent.Future
import scala.language.implicitConversions

sealed trait Result

object Result {

  case class ResInt(v: Int) extends Result

  case class ResFloat(v: Float) extends Result

  case class ResDouble(v: Double) extends Result

  case class ResBoolean(v: Boolean) extends Result

  case class ResString(v: String) extends Result

  case class ResArray(v: Array[Result]) extends Result

  case class ResMap(v: Map[String, Result]) extends Result

  case class ResList(v: List[Result]) extends Result

  case class ResFuture(f: Future[Result]) extends Result

  object Null extends Result

  def apply(v: Result) = v

  def List(vals: Result*): Result = vals.toList
  def Map(vals: (String, Result)*): Result = vals.toMap

  implicit def resInt(v: Int): Result = ResInt(v)
  implicit def resFloat(v: Float): Result = ResFloat(v)
  implicit def resDouble(v: Double): Result = ResDouble(v)
  implicit def resBoolean(v: Boolean): Result = ResBoolean(v)
  implicit def resString(v: String): Result = ResString(v)
  implicit def resMap(v: Map[String, Result]): Result = ResMap(v)
  implicit def resList(v: List[Result]): Result = ResList(v)
  implicit def resFuture(v: Future[Result]): Result = ResFuture(v)
}