<?php
   $con=mysqli_connect("localhost","root","","garbagecollectionpoints");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   $sql = "SELECT id, name, latitude, longtitude, type, description, data FROM garbagepoint WHERE 1";

   $result = $con->query($sql);

   if ($result->num_rows > 0) {
      echo "getBins:success|";
      while($row = $result->fetch_assoc()) {
         echo $row["id"]." ".$row["name"]." ".$row["latitude"]." ".$row["longtitude"]." ".$row["type"]." ".$row["description"]." ".$row["data"]."|";
      }
   }
	
   mysqli_close($con);
?>