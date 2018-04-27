<?php
if($_SERVER["REQUEST_METHOD"]=="POST"){
	require 'connection.php';
	createStudent();
}
function createStudent()
{
	global $connect;
	
	$date = $_POST["date"];	
	$name = $_POST["name"];
	$address = $_POST["address"];
	$strength = $_POST["strength"];
	$start = $_POST["start"];
	
	$query = " Insert into bluetooth(date,name,address,strength,start) values ('$date','$name','$address', '$strength', '$start');";
	
	mysqli_query($connect, $query) or die (mysqli_error($connect));
	mysqli_close($connect);
	
}
?>
