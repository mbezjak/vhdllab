library ieee;
use ieee.std_logic_1164.all;
 
ENTITY VL_NOR IS 
  GENERIC (
    n: positive := 2;
    delay: time := 10 ns
  );
  PORT (
    a: IN  std_logic_vector(n-1 downto 0);
    f: OUT std_logic
  );
begin
  assert n >= 2;
END VL_NOR;
 
ARCHITECTURE behavioral OF VL_NOR IS
BEGIN
  process(a)
    variable res: std_logic;
  begin
    res := '0';
    for i in n-1 downto 0 loop
      res := res or a(i);
    end loop;
    f <= not res after delay;
  end process;
  
END behavioral;


