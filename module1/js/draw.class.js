import {
    getCoordinatesOnCanvas
} from "./utility.js";

export default class Draw {
    constructor(canvasId , btnId) {
        this.canvas = document.getElementById(canvasId);
        this.context = this.canvas.getContext("2d");
        this.btn = document.getElementById(btnId);
    }

    init() {
        this.canvas.onmousedown = e => this.onMouseDown(e);
        this.btn.onmouseup = ()=> this.onButtonClick();
    }

    onButtonClick(){
        this.context.clearRect(0, 0, this.canvas.clientWidth, this.canvas.clientHeight );
    }


    onMouseDown(e) {

        this.savedImageData = this.context.getImageData(0, 0, this.canvas.clientWidth, this.canvas.clientHeight);
        this.canvas.onmousemove = e => this.onMouseMove(e);
        this.canvas.onmouseup = e => this.onMouseUp(e);

        this.startPos = getCoordinatesOnCanvas(e, this.canvas);

    }

    onMouseMove(e) {
        this.currentPos = getCoordinatesOnCanvas(e, this.canvas);
        this.drawTriangle();
    }

    onMouseUp(e) {
        this.canvas.onmouseup = null;
        this.canvas.onmousemove = null;

        this.colorTriangle();
    }


    drawTriangle() {
        this.context.putImageData(this.savedImageData, 0, 0);
        let x = this.startPos.x + (this.currentPos.x - this.startPos.x) / 2;
        let y = this.startPos.y;

        this.context.beginPath();

        this.context.moveTo(x, y);
        this.context.lineTo(this.currentPos.x, this.currentPos.y);
        this.context.lineTo(this.startPos.x, this.currentPos.y);
        this.context.lineTo(x, y);

        this.context.fillStyle = 'yellow';
        this.context.stroke();
    }

    colorTriangle() {
        let red = Math.round(Math.random() * 255);
        let green = Math.round(Math.random() * 255);
        let blue = Math.round(Math.random() * 255);

        this.context.fillStyle = `rgb(${red},${green},${blue})`;
        this.context.fill();
    }
}