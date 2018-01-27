package it.polimi.genomics.importer.ModelDatabase

trait Donor extends Table{

  var sourceId: String = _

  var species : String = _

  var age : Int = _

  var gender : String= _

  var ethnicity : String = _

  _hasDependencies = true

  _dependenciesTables = List("BIOSAMPLE")


  override def insert(): Int ={
    dbHandler.insertDonor(this.sourceId,this.species,this.age,this.gender,this.ethnicity)
  }

  override def update(): Int ={
    dbHandler.updateDonor(this.sourceId,this.species,this.age,this.gender,this.ethnicity)
  }

  override def setForeignKeys(table: Table): Unit = {
  }

  override def checkInsert(): Boolean ={
    dbHandler.checkInsertDonor(this.sourceId)
  }

  override def checkConsistency(): Boolean = {
    if(this.sourceId != null) true else false
  }

  override def getId(): Int = {
    dbHandler.getDonorId(this.sourceId)
  }

  def convertTo(values: Seq[(String, Option[String], Option[Int], Option[String], Option[String])]): Unit = {
    if(values.length > 1)
      logger.error(s"Too many value: ${values.length}")
    else {
      var value = values.head
      this.sourceId = value._1
      if(value._2.isDefined) this.species = value._2.get
      if(value._3.isDefined) this.age = value._3.get
      if(value._4.isDefined) this.gender = value._4.get
      if(value._5.isDefined) this.ethnicity = value._5.get
    }
  }

  def writeInFile(path: String): Unit = {
    val write = getWriter(path)
    val tableName = "donor"

    write.append(getMessage(tableName + "__source_id", this.sourceId))
    if(this.species != null) write.append(getMessage(tableName + "__species", this.species))
    if(this.age != 0) write.append(getMessage(tableName + "__age", this.age))
    if(this.gender != null) write.append(getMessage(tableName + "__gender", this.gender))
    if(this.ethnicity != null) write.append(getMessage(tableName + "__ethnicity", this.ethnicity))

    flushAndClose(write)
  }


}
