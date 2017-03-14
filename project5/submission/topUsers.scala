sc.textFile("./twitter.edges").map( _.split(": ") ).flatMap( _(1).split(",").map( ( _, 1 ) ) ).reduceByKey(_ + _).filter(_._2 > 1000).saveAsTextFile("output")
System.exit(0)