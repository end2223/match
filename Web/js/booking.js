function start(){
    addBooking();
}

var api = "http://localhost:8800/bookings/add";

function createBooking(data, callback){
    var options = {
        method: "POST",
        body: data
    }
    fetch(api, options)
        .then((reponse)=>{
            return reponse;
        })
        .then(callback);
}

function checK(booking){
    console.log(booking["status"]);
    if(booking["status"]=="204"){
        alert("Succesfully!");
    }
    else{
        alert("Failure!");
    }
}


function addBooking(){
    var matchId = document.querySelector("#matchId").value;
    var ticketId = document.querySelector("#ticketId").value;
    var customerId = document.querySelector("#customerId").value;

    if(matchId.trim()!=""&&ticketId.trim()!=""&&customerId.trim()!=""){
        var data = {
            matchId: matchId,
            ticketId: ticketId,
            customerId: customerId
        }

        var d = JSON.stringify(data);
        var compo = document.querySelector(`#s_${ticketId}`).innerText;
        if(compo=="false"){
            alert("The ticket has been purchased!");
        }
        else{
            createBooking(d,checK)
        }
        //
        // var c = 0;
        // checkTicket(matchId, ticketId, d, checK, createBooking);

    }
    else{
        alert("Failure!");
    }
}



// var ticketApi = "http://localhost:8092/matches/get/available/";
// function checkTicket(mId, tId, callback){
//     var url = ticketApi+mId+"/"+tId;
//     fetch(url)
//         .then((reponse)=>{
//             return reponse.json();
//         })
//         .then(()=>{
//             callback();
//         })
//         .catch(()=>{
//             alert("The ticket has been purchased!");
//         })
// }
