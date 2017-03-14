val fileRdd = sc.textFile("./twitter.edges")
val splitRdd = fileRdd.map( line => line.split(": ") )
val yourRdd = splitRdd.flatMap( arr => {
	val following = arr(1)
	val celebs = following.split(",")
	celebs.map( celeb => ( celeb, 1 ) )
} )

val countRdd = yourRdd.reduceByKey(_+_)
val topRdd = countRdd.filter(_._2 > 1000)
topRdd.saveAsTextFile("output")
System.exit(0)
