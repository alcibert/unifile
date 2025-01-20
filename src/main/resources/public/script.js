let scanA = document.getElementById("scanA");
let scanB = document.getElementById("scanB");
let openExplorerA = document.getElementById("explorerButtonA");
let openExplorerB = document.getElementById("explorerButtonB");
let explorer = document.getElementById("explorer");
scanA.addEventListener("click", scan);
scanB.addEventListener("click", scan);
const apiURL = "/api/v1.0/";

document.addEventListener('submitPathA', (event) => {
   // const inputValue = event.detail.value;
    //Alci TODO: value in Textfeld eintragen entsprechendes - data field attribut dem filebrowser mitgeben,d er kann das dann uaslesen und richtig eintragen, dann brauchts keinenn EventlIstener
});

//Alci TODO analog für submitPathB

openExplorerA.addEventListener("click", function (e){
    explorer.style.display="flex";
});

function scan(e){
    let volume = e.target.dataset.volume;
    let path = document.getElementById(`pathSelector${volume.toUpperCase()}`).value;
    let url = `${apiURL}directory/scan/${volume}?path=${path}`;
    fetch(url, callback);
}
function callback(e){
    console.log(e);
}

