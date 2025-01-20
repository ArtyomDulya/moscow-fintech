package foundations



//package com.rockthejvm.foundations
//
//import cats.effect.{IO, IOApp, Resource}
//
//import java.io.{File, FileWriter, PrintWriter}
//import scala.io.StdIn
//import scala.concurrent.duration.*
//import scala.util.Random
//
//object CatsEffect extends IOApp.Simple {
//
//	val firstIO: IO[Int] = IO.pure(42)
//	val delayedIO: IO[Int] = IO.apply {
//		println("Hi")
//		42
//	}
//
//	// transformations
//	// map + flatMap
//	val improveMeaningOfLive = firstIO.map(_ * 2)
//	val printMeaningOfLive = firstIO.flatTap(mol => IO(println(mol)))
//	// for comprehensions
//	def smallProgram(): IO[Unit] = for {
//		line1 <- IO(StdIn.readLine())
//		line2 <- IO(StdIn.readLine())
//		_ <- IO(println(line1 + line2))
//	} yield ()
//
//
////	def evaluateIO[A](io: IO[A]): Unit =
////		import cats.effect.unsafe.implicits.global
////		val meaningForLife = io.unsafeRunSync()
////		println(s" Hi $meaningForLife")
//
//	//raise/"catch" errors
//	val aFailure: IO[Int] = IO.raiseError(new RuntimeException("a proper failure"))
//	val delaiWithIt = aFailure.handleErrorWith {
//		case _: RuntimeException => IO(println("I'm still here, no worries"))
//	}
//
//	// fibers = "lightweight threads"
//	val delayedPrint = IO.sleep(1.second) *> IO.println(Random.nextInt(100))
//	val manyPrints = for {
//		fib1 <- delayedPrint.start
//		fib2 <- delayedPrint.start
//		_ <- fib1.join
//		_<- fib2.join
//
//	} yield ()
//
//	val cancelledFiber = for {
//		fib <- delayedPrint.onCancel(IO(println("I'm cancelled!"))).start
//		_ <- IO.sleep(500.millis) *> IO(println("cancelling fiber")) *> fib.cancel
//		_ <- fib.join
//	} yield ()
//
//	// uncancelation
//	val ignoreCanccellation = for {
//		fib <- IO.uncancelable(_ => delayedPrint.onCancel(IO(println("I'm cancelled")))).start
//		_ <- IO.sleep(500.millis) *> IO(println("cancelling fiber")) *> fib.cancel
//		_ <- fib.join
//	} yield ()
//
//	// resources
//	val readingResource = Resource.make(
//		IO(scala.io.Source.fromFile("src/main/scala/com/rockthejvm/jobsboard/playground/foundations/CatsEffect.scala"))
//	)(source => IO(println("closing source")) *> IO(source.close()))
//	val readingEffect = readingResource.use(source => IO(source.getLines().foreach(println)))
//
//	// compose resources
//	val copiedFileResource = Resource.make(IO(new PrintWriter(new FileWriter(new File("src/main/resources/dumpedFile.scala"))))
//	)(writer => IO(println("closing duplicated file")) *> IO(writer.close()))
//
//	val compositeResource = for {
//		source <- readingResource
//		destination <- copiedFileResource
//	} yield (source, destination)
//
//	val copyFileEffect = compositeResource.use {
//		case (source, destination) => IO(source.getLines().foreach(destination.println))
//	}
//	override def run = copyFileEffect
//
////	def main(args: Array[String]): Unit = {
////		evaluateIO(smallProgram())
////	}
//}
