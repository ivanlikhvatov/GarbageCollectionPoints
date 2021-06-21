<?php
   $con=mysqli_connect("localhost","root","","garbagecollectionpoints");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
	
   $name = $_POST['name'];
   $latitude = $_POST['latitude'];
   $longtitude = $_POST['longtitude'];
   $binType = $_POST['binType'];
   $desc = $_POST['desc'];
   $date = $_POST['date'];

   $sql = "INSERT INTO garbagepoint (name, latitude, longtitude, type, description, data) VALUES ('$name', '$latitude', '$longtitude', '$binType', '$desc', '$date')";

   if (mysqli_query($con, $sql)) {
      echo "createBin:success";
   } else {
      echo "createBin:unsuccess";
   }
	
   mysqli_close($con);
?>