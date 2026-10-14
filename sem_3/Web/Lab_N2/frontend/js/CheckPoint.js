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

FORM.addEventListener('submit', async function (event) {
    event.preventDefault();

    const yInput = document.getElementById('coord_Y').value.trim();
    if( yInput.includes('/') || yInput.includes('\\') || yInput.split('.').length > 2 )
    {
        alert( 'Пожалуйста, введите корректное число для Y (-3..5) ' );
        return;
    }
    const yValue = yInput !== '' ? parseFloat( yInput ) : NaN;
    const rInputSelected = document.querySelector( 'input[name="radius"]:checked' );
    const rValue = rInputSelected ? parseFloat( rInputSelected.value ) : NaN;


    if( xValue == null ) 
    {
        alert('Пожалуйста, выберите значение X');
        return;
    }
    if( isNaN(yValue) ) 
    {
        alert('Пожалуйста, введите число для Y');
        return;
    }
    if( isNaN(rValue) ) 
    {
        alert('Пожалуйста, выберите значение R');
        return;
    }

    try 
    {
        const response = await fetch(`/fcgi-bin/lab2-server.jar?request=CHECK_POINT`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
                'Accept': 'application/json'
            },
            body: JSON.stringify(
            {
                //request: 'CHECK_POINT',
                x: xValue,
                y: yValue,
                r: rValue
            })
        });

        if( !response.ok ) 
        {
            let errorMessage = `Ошибка HTTP: ${response.status}`;
            const respText = await response.text();
            try 
            {
                const errorData = JSON.parse( respText );
                if( errorData && errorData.message ) 
                {
                    errorMessage = errorData.message;
                }
            } 
            catch( error ) 
            {
                if( respText )
                {
                    errorMessage = respText;
                }
            }
            throw new Error( errorMessage );
        }

        const data = await response.json();

        updateTable( data );
    } 
    catch( error ) 
    {
        console.error( 'Ошибка при выполнении AJAX-запроса:', error );
        if( error && error.message )
        {
            const mes = error.message.trim();
            if( mes == "Validate error: x" ) alert("Выберите значение x");
            else if( mes == "Validate error: y" ) alert("Введите корректное значение y (-3..5)");
            else if( mes == "Validate error: r" ) alert("Выберите значение r");
            else alert( 'Не удалось отправить данные на сервер' );
        }
        else alert( 'Не удалось отправить данные на сервер' );
    }
});

function formatY( y )
{
    if( y === null || y === undefined ) 
    {
        return "";
    }

    let strY = String( y ).trim();

    strY = strY.replace(',', '.');

    const numY = parseFloat( strY );
    if( isNaN(numY) ) 
    {
        return "";
    }

    return encodeURIComponent( strY );
}

function updateTable( result ) 
{
    const table = document.getElementById( 'results' );
    if( !table )
    {
        console.error('Таблица результатов не найдена в DOM');
        return;
    }
    const newRow = document.createElement( 'tr' );
    newRow.innerHTML = `
        <td> ${ result.x } </td>
        <td> ${ result.y } </td>
        <td> ${ result.r } </td>
        <td> ${ result.success == 'true' ? 'точка принадлежит' : 'точка не принадлежит' } </td>
        <td> ${ formatDate( result.time ) } </td>
    `;
    const container = table.querySelector( 'tbody' );
    container.prepend( newRow ); //appendChild
}

export function formatDate( dateString ) 
{
     const date = new Date( dateString );
     return new Intl.DateTimeFormat('ru-RU', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
     }).format(date);
}
