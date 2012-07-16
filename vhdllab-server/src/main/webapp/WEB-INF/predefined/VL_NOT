library ieee;
use ieee.std_logic_1164.all;
 
ENTITY VL_NOT IS 
  GENERIC (
    delay: time := 10 ns
  );
  PORT (
    a: IN  std_logic;
    f: OUT std_logic
  );
END VL_NOT;
 
ARCHITECTURE behavioral OF VL_NOT IS
BEGIN

    f <= not a after delay;
  
END behavioral;

