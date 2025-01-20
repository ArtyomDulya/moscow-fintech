//package foundations
//
//import org.http4s.ember.server.EmberServerBuilder
//import org.typelevel.ci.CIString
//
//import java.util.UUID
//
//object Http4s extends IOApp.Simple {
//
//	// simulate an HTTP  server with "students" and "courses"
//	type Student = String
//
//	case class Instructor(firstname: String,
//	                      lastname: String)
//
//	case class Course(id: String,
//	                  title: String,
//	                  year: Int,
//	                  students: List[Student],
//	                  instructorName: String)
//
//	object CourseRepository {
//		// a "database"
//		val catsEffectCourse = Course(
//			"f2983af4-b348-474a-bca9-857c5b37f2c6",
//			"Rock the JVM Ultimate Scala course",
//			2022,
//			List("Daniel", "Master Yoda"),
//			"Martin Odersky"
//		)
//		val courses: Map[String, Course] = Map(catsEffectCourse.id -> catsEffectCourse)
//
//		// API
//		def findCourseById(courseId: UUID): Option[Course] =
//			courses.get(courseId.toString)
//
//		def findCourseByInstructor(name: String): List[Course] =
//			courses.values.filter(_.instructorName == name).toList
//	}
//
//	// essential REST endpoints
//	// GET localhost:8080/courses?instructor=Martin%20Odersky&year=2022
//	// GET localhost:8080/courses/f2983af4-b348-474a-bca9-857c5b37f2c6/students
//
//	object InstructorQueryParamMatcher extends QueryParamDecoderMatcher[String]("instructor")
//
//	object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Int]("year")
//
//	def courseRoutes[F[_] : Monad]: HttpRoutes[F] = {
//		val dsl = Http4sDsl[F]
//
//		HttpRoutes.of[F] {
//			case GET -> Root / "courses" :? InstructorQueryParamMatcher(instructor) +& YearQueryParamMatcher(maybeYear) =>
//				val courses = CourseRepository.findCourseByInstructor(instructor)
//				maybeYear match {
//					case Some(y) => y.fold(
//						_ => BadRequest("Parameter 'year' invalid"),
//						year => Ok(courses.filter(_.year == year).asJson)
//					)
//					case None => Ok(courses.asJson)
//				}
//			case GET -> Root / "courses" / UUIDVar(courseId) / "students" =>
//				CourseRepository.findCourseById(courseId).map(_.students) match
//					case Some(students) => Ok(students.asJson, Header.Raw(CIString("My-custom-header"), "rockthejvm"))
//					case None => NotFound(s"No course with $courseId was found")
//		}
//	}
//
//	def healthEndpoint[F[_] : Monad]: HttpRoutes[F] = {
//		val dsl = Http4sDsl[F]
//		HttpRoutes.of[F] {
//			case GET -> Root / "health" => Ok("All going great!")
//		}
//	}
//
//	def allRoutes[F[_]: Monad]: HttpRoutes[F] = courseRoutes[F] <+> healthEndpoint[F]
//
//	def routerWithPathPrefixes = Router(
//		"/api" -> courseRoutes[IO],
//		"/private" -> healthEndpoint[IO]
//	).orNotFound
//
//	override def run = EmberServerBuilder
//	  .default[IO]
//	  .withHttpApp(routerWithPathPrefixes)
//	  .build
//	  .use(_ => IO.println("Server ready!") *> IO.never)
//}
