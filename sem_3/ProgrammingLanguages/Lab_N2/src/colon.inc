%define indicator 0

%macro colon 2
	%2:
		dq indicator
		db %1, 0
	%define indicator %2
%endmacro
