<?php
   $con=mysqli_connect("localhost","root","","garbagecollectionpoints");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
	
   $email = $_POST['email'];
   $name = $_POST['name'];
   $password = $_POST['password'];

   $sql = "INSERT INTO user (name, email, password) VALUES ('$name', '$email', '$password')";

   if (mysqli_query($con, $sql)) {
      echo "register:success";
   } else {
      echo "register:unsuccess";
   }
	
   mysqli_close($con);
?>