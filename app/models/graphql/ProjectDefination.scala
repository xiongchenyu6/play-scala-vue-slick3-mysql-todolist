package models.graphql

import sangria.schema._
import sangria.macros.derive._
import tables.Tables.ProjectRow
import models.ProjectRepo
object ProjectDefination {

val ProjectType: ObjectType[Unit, ProjectRow] =
  deriveObjectType[Unit,ProjectRow]()

val idArg = Argument("id", IntType)

val Query = ObjectType(
  "Query", fields[ProjectRepo, Unit](
    Field("project", ProjectType,
      arguments = idArg :: Nil,
      deprecationReason = Some("Test"),
      resolve = (ctx) ⇒ ctx.ctx.byId(ctx arg idArg))
  ))

  val StarWarsSchema = Schema(Query)
}
