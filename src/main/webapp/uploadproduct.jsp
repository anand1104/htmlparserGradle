<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Dashboard</title>

    <meta http-equiv="Content-Type"
          content="text/html; charset=utf-8"/>

    <link rel="stylesheet" type="text/css"
          href="css/style.css">
    <link rel="stylesheet" type="text/css"
          href="css/font-awesome.css">
    <link rel="stylesheet" type="text/css"
          href="css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css"
          href="css/bootstrap.min.css" media="screen">

    <link rel="stylesheet" href="css/jquery-ui.css">
    <script src="js/jquery-1.10.2.js"></script>
    <script src="js/jquery-ui.js"></script>

    <script type="text/javascript"
            src="js/jquery.min.js"></script>
    <script src="js/jquery-1.9.0.js"></script>
    <script src="js/jquery-ui-1.10.0.custom.js"></script>

    <script>

        function onChange(str) {
            var xmlhttp;
            if (window.XMLHttpRequest) {
//   			code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp = new XMLHttpRequest();
            } else {
// 				code for IE6, IE5
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            xmlhttp.onreadystatechange = function () {
                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                    document.getElementById("subcatdiv").innerHTML = xmlhttp.responseText;
                }
            }
            xmlhttp.open("GET", "jsps/div/subcat.jsp?id=" + str, true);
            xmlhttp.send();
        }
    </script>

</head>
<body>
<div class="auto-1000">
    <div class="main-container">
        <div class="tab-container">
            <img style="float: left;width: 160px;height: 65px;"
                 src="images/shopezzy.png">
            <ul>
                <!--          				<li><a id=""  href="logout" ><span>Logout</span></a></li> -->
            </ul>
        </div>
        <div class="report-container">
            <h2>Add Items</h2>
            <div class="select-form">

                <form action="update" method="post">
                    <table>

                        <tr>
                            <td>Company</td>
                            <td>
                                <input type="text"
                                       name="company"
                                       id="company"
                                       list="searchcompany"
                                       autocomplete="off">
                                <datalist
                                        id="searchcompany">
                                    <option>test</option>
                                    <option>testing</option>
                                    <option>test123</option>
                                </datalist>
                            </td>
                        </tr>
                        <tr>
                            <td>Brand</td>
                            <td>
                                <input type="text"
                                       name="brand"
                                       id="brand"
                                       list="searchbrand"
                                       autocomplete="off">
                                <datalist id="searchbrand">
                                    <option></option>
                                </datalist>
                            </td>
                        </tr>
                        <tr>
                            <td>Product</td>
                            <td>
                                <input type="text"
                                       name="product"
                                       id="product"
                                       list="searchbrand"
                                       autocomplete="off">
                                <datalist id="searchbrand">
                                    <option></option>
                                </datalist>
                            </td>
                        </tr>
                        <tr>
                            <td>Varient</td>
                            <td>
                                <input type="text"
                                       name="varient"
                                       id="varient">
                            </td>
                        </tr>
                        <tr>
                            <td>Perishable</td>
                            <td style="float: left;">
                                <input type="radio"
                                       name="perishable"
                                       id="perishable"
                                       value="1"
                                       style="margin-left: 24px;">Yes
                                <input type="radio"
                                       name="perishable"
                                       id="perishable"
                                       value="0"
                                       style="margin-left: 40px;">
                                No
                            </td>
                        </tr>
                        <tr>
                            <td>Image</td>
                            <td>
                                <input type="file"
                                       name="image"
                                       id="image">
                            </td>
                        </tr>
                        <tr>
                            <td>Category</td>
                            <td>
                                <select name="scat"
                                        id="scat"
                                        onchange="onChange(this.value);">
                                    <option value="-1">--
                                        Please-Select --
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr id="subcatdiv">
                            <td>Sub Category</td>
                            <td>
                                <select name="ssubcat"
                                        id="ssubcat">
                                    <option value="-1">--
                                        Please-Select --
                                    </option>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2">
                                <div class="more-varient"
                                     style='border: solid 2px #E48810;'>
                                    <table>
                                        <tbody>
                                        <tr>
                                            <td style="padding-right: 38px;">
                                                Varient
                                            </td>
                                            <td><input
                                                    type="text"
                                                    name="varient"
                                                    id="varient">
                                            </td>

                                        </tr>
                                        <tr>
                                            <td style="padding-right: 38px;">
                                                MRP
                                            </td>
                                            <td><input
                                                    type="text"
                                                    name="mrp"
                                                    id="mrp">
                                            </td>
                                        </tr>

                                        </tbody>
                                    </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="button"
                                       id="add-varient-button"
                                       style="background: #E48810;padding: 7px;color:white;float: right;-webkit-box-shadow: inser 0px 2px 0px #E48810;border: #E48810;"
                                       value="More-Varient">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="Submit"
                                       id="sbmtn"
                                       value="Submit">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var max_fields = 10; //maximum input boxes allowed
    var wrapper = $(".more-varient"); //Fields wrapper
    var add_button = $("#add-varient-button"); //Add button ID

    var x = 1; //initlal text box count
    $(add_button).click(function (e) { //on add input button click
        alert("hello");
        e.preventDefault();
        console.log("event triggered");
        if (x < max_fields) { //max input box allowed
            x++; //text box increment
//   		      $(wrapper).append('<div class="input-div" style="/*border-bottom: dashed 0.75px #C8C8C8;*/ margin-bottom: 10px;"><input type="text" style="width: 25%;" placeholder="Place Name" name="placename'+x+'" id="placename'+x+'" required>&nbsp;<input type="text" style="width: 10%;" placeholder="Latitude" name="latitude'+x+'" id="latitude'+x+'" required>&nbsp;<input type="text" style="width: 10%;" placeholder="Longitude" name="longitude'+x+'" id="longitude'+x+'" required>&nbsp;<input type="text" style="width: 10%;" placeholder="Radius" name="radius'+x+'" id="radius'+x+'" required><div class="mapimg" alt="map" onclick="openmap('+x+');" style="margin: 8px 12% 0px 0px; float: right;"><img src="/ce/images/map.png"></div><div class="remove-loc" style="float: right; margin: 6px -13% 0px 0px;"><img src="/ce/images/circle-close-32.png"></div></div>'); //add input box
            $(wrapper).append("<table><tbody ><tr><td style='padding-right: 38px;'>Varient</td><td><input type='text' name='varient' id='varient'></td><td><div class='remove-varient' style='float: right; margin: 6px -13% 0px 0px;'><img src='images/remove.png' style='width: 15px;'></div></td></tr><tr><td style='padding-right: 38px;'>MRP</td><td><input type='text' name='mrp' id='mrp' ></td></tr></tbody></table>"); //add input box
        }
    });

    $(wrapper).on("click", ".remove-varient", function (e) { //user click on remove text
        e.preventDefault();
        $(this).parent('div').remove();
        x--;
    })

</script>
</body>
</html>
