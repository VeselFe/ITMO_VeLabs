const R = 100;

const X0 = 300;
const Y0 = 200;
const weight = 250;
const height = 175;
const canvas = document.getElementById("canvas");
const creatorField = canvas.getContext("2d");

// Оси
creatorField.beginPath();
creatorField.moveTo(X0, Y0);
creatorField.lineTo(X0, Y0 - height);
creatorField.lineTo(X0, Y0 + height);
creatorField.moveTo(X0, Y0);
creatorField.lineTo(X0 + weight, Y0);
creatorField.lineTo(X0 - weight, Y0);
creatorField.strokeStyle = "#333";
creatorField.stroke();

// стрелка ox
creatorField.beginPath();
creatorField.moveTo(X0 + weight, Y0);
creatorField.lineTo(X0 + weight - 10, Y0 - 5);
creatorField.lineTo(X0 + weight - 10, Y0 + 5);
creatorField.closePath();
creatorField.fillStyle = "#000";
creatorField.fill();

// стрелка oy
creatorField.beginPath();
creatorField.moveTo(X0, Y0 - height);
creatorField.lineTo(X0 - 5, Y0 - height + 10);
creatorField.lineTo(X0 + 5, Y0 - height + 10);
creatorField.closePath();
creatorField.fillStyle = "#000";
creatorField.fill();

// Метк возле стрлок
creatorField.font = "14px Arial";
creatorField.fillStyle = "#000";
creatorField.fillText("X", X0 + weight - 15, Y0 - 10);
creatorField.fillText("Y", X0 + 10, Y0 - height + 15);

// квадрат
creatorField.fillStyle = "#5BC0EB";
creatorField.fillRect(X0, Y0, -R, R);

// Треугольник
creatorField.beginPath();
creatorField.moveTo(X0, Y0);
creatorField.lineTo(X0, Y0 + R/2);
creatorField.lineTo(X0 + R/2, Y0);
creatorField.closePath();
creatorField.fillStyle = "#5BC0EB";
creatorField.fill();

// Сектор круга
creatorField.beginPath();
creatorField.moveTo(X0, Y0);
creatorField.arc(
    X0,
    Y0,
    R / 2,
    Math.PI,
    Math.PI * 1.5
);
creatorField.closePath();
creatorField.fillStyle = "#5BC0EB";
creatorField.fill();

drawMarks(creatorField, X0, Y0, R);

function drawMarks(creatorMarks, X0, Y0, R) 
{
    creatorMarks.font = "12px Arial";
    creatorMarks.fillStyle = "#000";
    creatorMarks.strokeStyle = "#000";
    creatorMarks.lineWidth = 1;

    const markLen = 3;

    const xPoints = [
        { val: -R, text: "-R" },
        { val: -R / 2, text: "-R/2" },
        { val: R / 2, text: "R/2" },
        { val: R, text: "R" }
    ];

    xPoints.forEach( p => {
        creatorMarks.beginPath();
        creatorMarks.moveTo(X0 + p.val, Y0 - markLen);
        creatorMarks.lineTo(X0 + p.val, Y0 + markLen);
        creatorMarks.stroke();
        creatorMarks.fillText(p.text, X0 + p.val - 10, Y0 - 10);
    });

    const yPoints = [
        { val: -R, text: "-R" },
        { val: -R / 2, text: "-R/2" },
        { val: R / 2, text: "R/2" },
        { val: R, text: "R" }
    ];

    yPoints.forEach(p => {
        creatorMarks.beginPath();
        creatorMarks.moveTo(X0 - markLen, Y0 - p.val);
        creatorMarks.lineTo(X0 + markLen, Y0 - p.val);
        creatorMarks.stroke();
        creatorMarks.fillText(p.text, X0 + 10, Y0 - p.val + 4);
    });
}