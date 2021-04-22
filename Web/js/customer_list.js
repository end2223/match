


var maxRow;
var customersApi = "http://localhost:8800/customers";
var addCustomersApi = "http://localhost:8800/customers/add";
var formRow =  `
            <tr id="tr_add">
                <th style="color: #999; outline: #fff;" contenteditable="true">Click here!</th>
                <th style="color: #999; outline: #fff;" contenteditable="true" id="idC"></th>
                <th style="color: #999; outline: #fff;" contenteditable="true" id="nameC"></th>
                <th style="color: #999; outline: #fff;" contenteditable="true"></th>
            </tr>
`

function start(){
    getCustomer(insertMultiRow);

    addCustomer();
}
start();

function getCustomer(callback){
    fetch(customersApi)
        .then((reponse)=>{
            return reponse.json();
        })
        .then(callback);
}

function createCustomer(data, callback){
    var options = {
        method: "POST",
        body: data
    }
    fetch(addCustomersApi, options)
        .then((reponse)=>{
            return reponse.json();
        })
        .then(callback);
}

function insertMultiRow(customers){
    maxRow = customers.length;
    var listRow = document.getElementById("tr_body");
    var htmls = customers.map((customer, i)=>{
        return `
            <tr>
                <th>${i+1}</th>
                <th>${customer["id"]}</th>
                <th>${customer["name"]}</th>
                <th></th>
            </tr>
        `;
    });
    htmls.push(formRow);
    listRow.innerHTML = htmls.join("");
}

function insertOneRow(customer){
    maxRow+=1;
    $("#tr_add").before(
        `
            <tr>
                <th>${maxRow}</th>
                <th>${customer["id"]}</th>
                <th>${customer["name"]}</th>
                <th></th>
            </tr>
        `
    )
}

function addCustomer(){
    var btnClick = document.querySelector("#btnAddClick");
    btnClick.onclick = ()=>{
        var id = document.querySelector("#idC").innerText;
        var name = document.querySelector("#nameC").innerText;

        if(id.trim()!=""&&name.trim()!=""){
            data = {
                id: id,
                name: name
            }
            var d = JSON.stringify(data);

            createCustomer(d, insertOneRow);
            alert("Succesfully!");
        }
        else{
            alert("Missing id or name!");
        }
    }
}
