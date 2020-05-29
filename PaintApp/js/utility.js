import Point from './point.model.js'

export function getMouseCoordinatesOnCanvas(e , canvas){
    let rect = canvas.getBoundingClientRect();
    let x = Math.round(e.clientX - rect.left);
    let y = Math.round(e.clientY - rect.top);

    return new Point(x,y);
}

export function getDistance(startPos , currentPos){
    let x = Math.pow(currentPos.x-startPos.x, 2);
    let y = Math.pow(currentPos.y-startPos.y, 2);

    let radius = Math.sqrt(x+y);

    return radius;
}