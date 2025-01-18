let scanA = document.getElementById("scanA");
let scanB = document.getElementById("scanB");
scanA.addEventListener("click", scan);
scanB.addEventListener("click", scan);
const apiURL = "/api/v1.0/";

function scan(e){
    let volume = e.target.dataset.volume;
    let path = document.getElementById(`pathSelector${volume.toUpperCase()}`).value;
    let url = `${apiURL}directory/scan/${volume}?path=${path}`;
    fetch(url, callback);
}
function callback(e){
    console.log(e);
}