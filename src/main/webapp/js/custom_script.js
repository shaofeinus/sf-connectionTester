/**
 * 
 */

function initEDServiceName() {
	// TODO: implement
	$('#EDservice').html("Dummy");
}

function initTestButton() {
	$("#testButton").click(
			function() {
				if ($('#addressInput').val() == null || $('#addressInput').val() == "") {
					alert("No host address entered!");
					return;
				}
				
				if (confirm("Test connection for '" + $('#addressInput').val()
						+ "' port " + $('#portInput').val() + "?")) {
					
					$('#hostInfo').html('Test connection for <strong>'+$('#addressInput').val()+'</strong>');
					
					$.get(
							'action/get_ip', 
							{'host': $('#addressInput').val()}, 
							function(data) {
								for(var i = 0; i < data.length; i++) {
									console.log(data[i].address);
									$('#resultList').append(
											'<li class="list-group-item">IP address: <strong>' + 
											data[i].address + 
											'</strong><div class="pull-right"><span id="addressStatus_' + i + '" class="glyphicon glyphicon-remove"></span><div></li>');
									$.get(
											'action/test_connection', 
											{
												'host': $('#addressInput').val(),
												'port': $('#portInput').val()
											},
											function(data) {
												$('#addressStatus_' + i).html("OK");
												console.log(data.reachable);
											}
									)
								}
							}
					);
				}
			})
}

$(document).ready(function() {
	initEDServiceName();
	initTestButton();
});
