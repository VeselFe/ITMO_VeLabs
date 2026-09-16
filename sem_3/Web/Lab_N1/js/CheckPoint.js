const FORM = document.getElementById('parameters');
const BUTTONS_FOR_X = document.querySelectorAll('.buttonX');

let xValue = null;

BUTTONS_FOR_X.forEach( button => {
    button.addEventListener('click', function() {
        BUTTONS_FOR_X.forEach( button => button.classList.remove('active'));
        this.classList.add('active');
        xValue = parseFloat(this.value);
    });
});

FORM.addEventListener('submit', function(event) {
    event.preventDefault();
    const yInput = document.getElementById('coord_Y').value.trim();
    if(yInput.includes('/') || yInput.includes('\\') || yInput.split('.').length > 2)
    {
        alert('Пожалуйста, введите корректное число для Y (-3..5) ');
        return;
    }
    
    const yValue = yInput !== '' ? parseFloat(yInput) : NaN;

    const rInputSelected = document.querySelector('input[name="radius"]:checked');
    const rValue = rInputSelected ? parseFloat(rInputSelected.value) : NaN;

    if( xValue === null ) 
    {
        alert('Пожалуйста, выберите значение X');
        return;
    }

    if( isNaN(yValue) || yValue < -3 || yValue > 5 ) 
    {
        alert('Пожалуйста, введите корректное число для Y (-3..5) ');
        return;
    }

    if( isNaN(rValue) ) 
    {
        alert('Пожалуйста, выберите значение R');
        return;
    }

    save(xValue, yValue, rValue, checkPoint(xValue, yValue, rValue));
});

function checkPoint(X, Y, R) 
{
    if( X <= 0 && Y >= 0 && (X ** 2 + Y ** 2 <= (R / 2) ** 2) )  
    {
        return true;
    }
    if( X >= -R && X <= 0 && Y >= -R && Y <= 0 ) 
    {
        return true;
    }
    if( X >= 0 && Y <= 0 && (X - Y <= R / 2) ) 
    {
        return true;
    }
    return false;
}