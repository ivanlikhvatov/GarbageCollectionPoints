<?php
   $con=mysqli_connect("localhost","root","","garbagecollectionpoints");

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
	
   $email = $_POST['email'];
   $password = $_POST['password'];
   $result = mysqli_query($con,"SELECT id FROM user WHERE email='$email' AND password='$password'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

   if ($data) {
      echo "login:success";
   } else {
      echo "login:unsuccess";
   }
	
   mysqli_close($con);
?>