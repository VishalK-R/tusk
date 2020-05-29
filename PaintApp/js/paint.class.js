import Point from './point.model.js'
import Fill from './fill.class.js';

import {
    TOOL_LINE,
    TOOL_RECTANGLE,
    TOOL_CIRCLE,
    TOOL_TRIANGLE,
    TOOL_PAINT_BUCKET,
    TOOL_PENCIL,
    TOOL_BRUSH,
    TOOL_ERASER
} from './tool.js';

import {
    getMouseCoordinatesOnCanvas,
    getDistance
} from './utility.js'

export default class Paint {

    constructor(canvasId) {
        this.canvas = document.getElementById(canvasId);
        this.context = canvas.getContext("2d");
        this.undoStack = [];
        this.undoLimit = 3;
    }

    set activeTool(tool) {
        this.tool = tool;
    }


    set lineWidth(linewidth) {
        this.linewidth = linewidth;
        this.context.lineWidth = this.linewidth;
    }

    set brushSize(brushsize) {
        this.brushsize = brushsize;
    }

    set selectedColor(color) {
        this.color = color;
        this.context.strokeStyle = this.color;
    }

    init() {
        this.canvas.onmousedown = e => this.onMouseDown(e);
    }

    onMouseDown(e) {
        this.savedData = this.context.getImageData(0, 0, this.canvas.clientWidth, this.canvas.clientHeight);

        if(this.undoStack.length >= 3) this.undoStack.shift();
        this.undoStack.push(this.savedData);

        this.canvas.onmousemove = e => this.onMouseMove(e);
        document.onmouseup = e => this.onMouseUp(e);

        this.startPos = getMouseCoordinatesOnCanvas(e, this.canvas);


        if (this.tool === TOOL_PENCIL || this.tool === TOOL_BRUSH) {
            this.context.beginPath();
            this.context.moveTo(this.startPos.x, this.startPos.y);
        } else if (this.tool === TOOL_PAINT_BUCKET) {
            //fill color
            new Fill(this.canvas, this.startPos, this.color);
        } else if (this.tool === TOOL_ERASER) {
            this.context.clearRect(this.startPos.x, this.startPos.y, this.brushsize, this.brushsize);
        }




    }

    onMouseMove(e) {


        this.currentPos = getMouseCoordinatesOnCanvas(e, this.canvas);

        switch (this.tool) {
            case TOOL_LINE:
            case TOOL_RECTANGLE:
            case TOOL_CIRCLE:
            case TOOL_TRIANGLE:
                this.drawShape();
                break;
            case TOOL_PENCIL:
                this.drawFreeLine(this.linewidth);
                break;
            case TOOL_BRUSH:
                this.drawFreeLine(this.brushsize);
                break;
            case TOOL_ERASER:
                this.context.clearRect(this.currentPos.x, this.currentPos.y, this.brushsize, this.brushsize);
            default:
                break;
        }
    }

    onMouseUp(e) {
        this.canvas.onmousemove = null;
        this.canvas.onmouseup = null;
    }

    drawShape() {
        this.context.putImageData(this.savedData, 0, 0);
        this.context.beginPath();

        if (this.tool == TOOL_LINE) {
            this.context.moveTo(this.startPos.x, this.startPos.y);
            this.context.lineTo(this.currentPos.x, this.currentPos.y);
        } else if (this.tool == TOOL_RECTANGLE) {
            this.context.rect(this.startPos.x, this.startPos.y, this.currentPos.x - this.startPos.x, this.currentPos.y - this.startPos.y);
        } else if (this.tool == TOOL_CIRCLE) {
            let radius = getDistance(this.startPos, this.currentPos);
            this.context.arc(this.startPos.x, this.startPos.y, radius, 0, 2 * Math.PI, false);
        } else if (this.tool == TOOL_TRIANGLE) {
            let startX = this.startPos.x + (this.currentPos.x - this.startPos.x) / 2;
            let startY = this.startPos.y;
            this.context.moveTo(startX, startY);
            this.context.lineTo(this.currentPos.x, this.currentPos.y);
            this.context.lineTo(this.startPos.x, this.currentPos.y);
            this.context.lineTo(startX, startY);
        }
        this.context.stroke();
    }

    drawFreeLine(linewidth) {
        this.context.lineWidth = linewidth;
        this.context.lineTo(this.currentPos.x, this.currentPos.y);
        this.context.stroke();
    }

    undoPaint(){
        if(this.undoStack.length > 0){
            this.context.putImageData(this.undoStack[this.undoStack.length-1],0,0);
            this.undoStack.pop();
        }else {
            alert("no undo available")
        }
    }
}