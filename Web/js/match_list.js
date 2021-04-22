var maxRow;
var matchesApi = "http://localhost:8800/matches"
var addMatchesApi = "http://localhost:8800/matches/add"
var formRow = `
                <tr id="tr_add">
                    <th style="color: #999; outline: #fff" contenteditable="true">"Click here!"</th>
                    <th style="color: #999; outline: #fff" contenteditable="true" id="idM"></th>
                    <th style="color: #999; outline: #fff" contenteditable="true" id="noT"></th>
                    <th style="color: #999; outline: #fff" contenteditable="true" id="name"></th>
                    <th style="color: #999; outline: #fff" contenteditable="true" id="date"></th>
                    <th></th>
                </tr>
`
function start(){
    getMatches(insertMultiRow);

    addMatch();
}
// start();

function getMatches(callback){
    fetch(matchesApi)
        .then((reponse)=>{
            return reponse.json();
        })
        .then(callback);
}

function createMatch(data, callback){
    var options = {
        method: "POST",
        body: data
    }
    fetch(addMatchesApi, options)
        .then((reponse)=>{
            return reponse.json();
        })
        .then(callback);
}

function insertMultiRow(matches){
    maxRow = matches.length;
    var listRow = document.querySelector("#tr_body");
    var htmls = matches.map((match, i)=>{
        return `
            <tr>
                <th>${i+1}</th>
                <th>${match["id"]}</th>
                <th>${match["numberOfTicket"]}</th>
                <th>${match["name"]}</th>
                <th>${match["date"]}</th>
                <th><a href="">Detail</a></th>
            </tr>
        `
    })
    htmls.push(formRow);
    listRow.innerHTML = htmls.join("");
}

function insertOneRow(match){
    maxRow+=1;
    $("#tr_add").before(`
        <tr>
            <th>${maxRow}</th>
            <th>${match["id"]}</th>
            <th>${match["numberOfTicket"]}</th>
            <th>${match["name"]}</th>
            <th>${match["date"]}</th>
            <th><a href="">Detail</a></th>
        </tr>
    `);
}

function addMatch(){
    var btnClick = document.querySelector("#addClick");

    btnClick.onclick=()=>{
        var id = document.querySelector("#idM").innerText;
        var numberOfTicket = document.querySelector("#noT").innerText;
        var name = document.querySelector("#name").innerText;
        var date = document.querySelector("#date").innerText;
        if(id.trim()!="" && numberOfTicket.trim()!="" ){
            var data = {
                id: id,
                numberOfTicket: numberOfTicket,
                name: name,
                date: date
            }
            var d = JSON.stringify(data);
            createMatch(d,insertOneRow);
            alert("Succesfully!");
        }
        else{
            alert("Missing id or...!")
        }
    };
}


//render ticket
function trueTickets(matches){
    var m = matches.find((match)=>{
        return match["id"] == checkM;
    });
    if(m==null){
        alert("There is not match!");
    }
    else{
        var tickets = m["ticketList"];
        tickets = tickets.filter((ticket)=>{
            return ticket["status"]==true;
        })
        render(tickets, m["name"], m["id"]);
    }
}

function falseTickets(matches){
    var m = matches.find((match)=>{
        return match["id"] == checkM;
    });
    if(m==null){
        alert("There is not match!");
    }
    else{
        var tickets = m["ticketList"];
        tickets = tickets.filter((ticket)=>{
            return ticket["status"]==false;
        })
        render(tickets, m["name"], m["id"]);
    }
}

function allTickets(matches){
    var m = matches.find((match)=>{
        return match["id"] == checkM;
    });
    if(m==null){
        alert("There is not match!");
    }
    else{
        var tickets = m["ticketList"];
        render(tickets, m["name"], m["id"]);
    }
}

function render(tickets, name, id){
    var htmls = tickets.map((ticket)=>{
        return`
            <tr>
                <th>${ticket["id"]}</th>
                <th>${ticket["date"]}</th>
                <th id = "s_${ticket["id"]}">${ticket["status"]}</th>
            </tr>
        `;
    })
    htmls.unshift(`
        <h2>ID: ${id} | ${name}</h2>
        <div style="margin-bottom: 10px;">
            <button onclick="availableT()">Available</button>
            <button onclick="overT()">Over</button>
            <button onclick="allT()">All</button>
        </div>
        <table class="table table-hover table-striped table-condensed table-bordered" >
            <thead>
                <tr>
                    <th>Ticket ID</th>
                    <th>Date</th>
                    <th >Status</th>
                </tr>
            </thead>
            <tbody id="tr_body">
    `);
    htmls.push(`
            </tbody>
        </table>
    `);

    var tb = document.querySelector("#_table");
    tb.innerHTML = htmls.join("");

}

var checkM;
function renderTickets(check, match){
    var m = document.querySelector("#search");
    m.value = match;
    checkM = match;
    if(check=="true"){
        console.log(check)
        getMatches(trueTickets);
    }
    else if(check=="false"){
        console.log(check)
        getMatches(falseTickets);
    }
    else{
        console.log(check)
        getMatches(allTickets);
    }

}

function clickSearch(){
    var m = document.querySelector("#search").value;
    renderTickets("all", m);
}

function availableT(){
    var m = document.querySelector("#search").value;
    renderTickets("true", m);
}
function overT(){
    var m = document.querySelector("#search").value;
    renderTickets("false", m);
}
function allT(){
    var m = document.querySelector("#search").value;
    renderTickets("all", m);
}